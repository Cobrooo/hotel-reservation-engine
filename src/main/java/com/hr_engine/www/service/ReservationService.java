package com.hr_engine.www.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hr_engine.www.dto.HoldRequestDto;
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
}