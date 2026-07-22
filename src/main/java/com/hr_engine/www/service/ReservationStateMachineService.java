package com.hr_engine.www.service;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.hr_engine.www.enums.ReservationStatus;
import com.hr_engine.www.exception.InvalidStateTransitionException;

@Service
public class ReservationStateMachineService {

    private static final Map<ReservationStatus, Set<ReservationStatus>> ALLOWED_TRANSITIONS = new EnumMap<>(ReservationStatus.class);

    static {
        ALLOWED_TRANSITIONS.put(ReservationStatus.HELD,
                EnumSet.of(ReservationStatus.PAYMENT_PROCESSING, ReservationStatus.RELEASED));
        ALLOWED_TRANSITIONS.put(ReservationStatus.PAYMENT_PROCESSING,
                EnumSet.of(ReservationStatus.CONFIRMED, ReservationStatus.FAILED));
        ALLOWED_TRANSITIONS.put(ReservationStatus.CONFIRMED, EnumSet.noneOf(ReservationStatus.class));
        ALLOWED_TRANSITIONS.put(ReservationStatus.FAILED, EnumSet.noneOf(ReservationStatus.class));
        ALLOWED_TRANSITIONS.put(ReservationStatus.RELEASED, EnumSet.noneOf(ReservationStatus.class));
        ALLOWED_TRANSITIONS.put(ReservationStatus.PENDING,
                EnumSet.of(ReservationStatus.CONFIRMED, ReservationStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(ReservationStatus.CANCELLED, EnumSet.noneOf(ReservationStatus.class));
    }

    public void validateTransition(ReservationStatus currentStatus, ReservationStatus newStatus) {
        Set<ReservationStatus> allowed = ALLOWED_TRANSITIONS.getOrDefault(currentStatus, EnumSet.noneOf(ReservationStatus.class));
        if (!allowed.contains(newStatus)) {
            throw new InvalidStateTransitionException(
                    "Cannot transition reservation from " + currentStatus + " to " + newStatus);
        }
    }
    
}