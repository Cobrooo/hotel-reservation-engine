package com.hr_engine.www.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hr_engine.www.entity.Reservation;
import com.hr_engine.www.entity.Room;
import com.hr_engine.www.enums.ReservationStatus;
import com.hr_engine.www.repository.ReservationRepository;
import com.hr_engine.www.repository.RoomRepository;

@Service
public class AvailabilityService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    // Statuses that block a room from being booked
    private static final List<ReservationStatus> ACTIVE_STATUSES =
            Arrays.asList(ReservationStatus.CONFIRMED, ReservationStatus.PENDING);

    public boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        validateDates(checkIn, checkOut);
        List<Reservation> overlaps = reservationRepository.findOverlappingReservations(
                roomId, checkIn, checkOut, ACTIVE_STATUSES);
        return overlaps.isEmpty();
    }

    public List<Room> findAvailableRoomsByRoomType(Long roomTypeId, LocalDate checkIn, LocalDate checkOut) {
        validateDates(checkIn, checkOut);

        List<Room> allRoomsOfType = roomRepository.findByRoomTypeId(roomTypeId);

        return allRoomsOfType.stream()
                .filter(room -> isRoomAvailable(room.getId(), checkIn, checkOut))
                .collect(Collectors.toList());
    }

    private void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Check-in and check-out dates are required");
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
        if (checkIn.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Check-in date cannot be in the past");
        }
    }
}