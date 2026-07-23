package com.hr_engine.www.service;

import com.hr_engine.www.entity.Reservation;
import com.hr_engine.www.enums.ReservationStatus;
import com.hr_engine.www.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReservationCleanupTask {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private BookingLockService bookingLockService;

    @Value("${booking.lock.hold-duration-minutes:5}")
    private long holdDurationMinutes;

    // Runs every 60 seconds
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void releaseExpiredHolds() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(holdDurationMinutes);

        List<Reservation> staleReservations = reservationRepository.findStaleHeldReservations(cutoffTime);

        if (staleReservations.isEmpty()) {
            return;
        }

        System.out.println("[Cleanup] Found " + staleReservations.size() + " stale HELD reservation(s) to release.");

        for (Reservation reservation : staleReservations) {
            reservation.setStatus(ReservationStatus.RELEASED);
            reservationRepository.save(reservation);

            // Defensive: also ensure the Redis lock is cleared, in case it somehow
            // outlived its TTL or is still held for any reason
            bookingLockService.unlockRoom(reservation.getRoom().getId());

            System.out.println("[Cleanup] Released reservation id=" + reservation.getId()
                    + " for room id=" + reservation.getRoom().getId());
        }
    }
    
    
}