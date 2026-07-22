package com.hr_engine.www.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hr_engine.www.dto.HoldRequestDto;
import com.hr_engine.www.dto.PayRequestDto;
import com.hr_engine.www.dto.PaymentRequestDto;
import com.hr_engine.www.dto.PaymentResult;
import com.hr_engine.www.entity.Reservation;
import com.hr_engine.www.entity.Room;
import com.hr_engine.www.enums.ReservationStatus;
import com.hr_engine.www.exception.ResourceNotFoundException;
import com.hr_engine.www.exception.RoomCurrentlyHeldException;
import com.hr_engine.www.repository.ReservationRepository;
import com.hr_engine.www.repository.RoomRepository;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AvailabilityService availabilityService;

    @Autowired
    private BookingLockService bookingLockService;
    
    @Autowired
    private PaymentGatewayService paymentGatewayService;

    @Autowired
    private ReservationStateMachineService stateMachineService;
    
    public Reservation holdRoom(HoldRequestDto request) {
        Long roomId = request.getRoomId();

        // 1. Confirm the room actually exists
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));

        // 2. Check availability BEFORE attempting to lock (fail fast, avoid unnecessary lock churn)
        boolean available = availabilityService.isRoomAvailable(
                roomId, request.getCheckInDate(), request.getCheckOutDate());

        if (!available) {
            throw new IllegalArgumentException("Room is not available for the selected dates");
        }

        // 3. Attempt to acquire the distributed lock
        boolean lockAcquired = bookingLockService.tryLockRoom(roomId);

        if (!lockAcquired) {
            throw new RoomCurrentlyHeldException(
                    "Room currently held by another user. Please try again shortly.");
        }

        // 4. Lock acquired — create the HELD reservation
        try {
            Reservation reservation = new Reservation();
            reservation.setRoom(room);
            reservation.setCheckInDate(request.getCheckInDate());
            reservation.setCheckOutDate(request.getCheckOutDate());
            reservation.setStatus(ReservationStatus.HELD);
            reservation.setGuestName(request.getGuestName());
            reservation.setGuestEmail(request.getGuestEmail());

            return reservationRepository.save(reservation);

        } catch (Exception e) {
            // If saving fails for any reason, release the lock immediately —
            // don't leave the room locked with no reservation to show for it
            bookingLockService.unlockRoom(roomId);
            throw e;
        }
    }
    
    public Reservation processPayment(Long reservationId, PayRequestDto payRequest) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + reservationId));

        // Step 1: HELD -> PAYMENT_PROCESSING
        stateMachineService.validateTransition(reservation.getStatus(), ReservationStatus.PAYMENT_PROCESSING);
        reservation.setStatus(ReservationStatus.PAYMENT_PROCESSING);
        reservationRepository.save(reservation);

        // Step 2: Call the mock payment gateway
        double amount = reservation.getRoom().getRoomType().getBasePrice().doubleValue();
        PaymentResult result = paymentGatewayService.processPayment(
                new PaymentRequestDto() {{
                    setSimulateFailure(payRequest.isSimulateFailure());
                    setCardNumber(payRequest.getCardNumber());
                }},
                amount
        );

        Long roomId = reservation.getRoom().getId();

        // Step 3: Transition based on payment outcome
        if (result.isSuccess()) {
            stateMachineService.validateTransition(reservation.getStatus(), ReservationStatus.CONFIRMED);
            reservation.setStatus(ReservationStatus.CONFIRMED);
        } else {
            stateMachineService.validateTransition(reservation.getStatus(), ReservationStatus.FAILED);
            reservation.setStatus(ReservationStatus.FAILED);
        }

        Reservation saved = reservationRepository.save(reservation);

        // Step 4: Release the Redis lock either way - booking is now permanent (CONFIRMED)
        // or the room needs to go back to the available pool (FAILED)
        bookingLockService.unlockRoom(roomId);

        return saved;
    }
}