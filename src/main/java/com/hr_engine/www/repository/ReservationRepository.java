package com.hr_engine.www.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hr_engine.www.entity.Reservation;
import com.hr_engine.www.enums.ReservationStatus;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r " +
           "WHERE r.room.id = :roomId " +
           "AND r.status IN :activeStatuses " +
           "AND r.checkInDate < :checkOutDate " +
           "AND r.checkOutDate > :checkInDate")
    List<Reservation> findOverlappingReservations(
            @Param("roomId") Long roomId,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("activeStatuses") List<ReservationStatus> activeStatuses
    );

    // Useful for Week 2/3 - find reservations by room type across all rooms
    @Query("SELECT r FROM Reservation r " +
           "WHERE r.room.roomType.id = :roomTypeId " +
           "AND r.status IN :activeStatuses " +
           "AND r.checkInDate < :checkOutDate " +
           "AND r.checkOutDate > :checkInDate")
    List<Reservation> findOverlappingReservationsByRoomType(
            @Param("roomTypeId") Long roomTypeId,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("activeStatuses") List<ReservationStatus> activeStatuses
    );
    
    
    @Query("SELECT r FROM Reservation r WHERE r.status = 'HELD' AND r.createdAt < :cutoffTime")
    List<Reservation> findStaleHeldReservations(@Param("cutoffTime") LocalDateTime cutoffTime);
    
    
}