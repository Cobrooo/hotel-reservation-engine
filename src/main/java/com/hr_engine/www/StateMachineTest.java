package com.hr_engine.www;

import com.hr_engine.www.enums.ReservationStatus;
import com.hr_engine.www.service.ReservationStateMachineService;

public class StateMachineTest {
    public static void main(String[] args) {
        ReservationStateMachineService service = new ReservationStateMachineService();

        // Valid transitions - should not throw
        service.validateTransition(ReservationStatus.HELD, ReservationStatus.PAYMENT_PROCESSING);
        System.out.println("HELD -> PAYMENT_PROCESSING: OK");

        service.validateTransition(ReservationStatus.PAYMENT_PROCESSING, ReservationStatus.CONFIRMED);
        System.out.println("PAYMENT_PROCESSING -> CONFIRMED: OK");

        // Invalid transition - should throw
        try {
            service.validateTransition(ReservationStatus.CONFIRMED, ReservationStatus.HELD);
            System.out.println("ERROR: should have thrown!");
        } catch (Exception e) {
            System.out.println("CONFIRMED -> HELD correctly rejected: " + e.getMessage());
        }

        try {
            service.validateTransition(ReservationStatus.HELD, ReservationStatus.CONFIRMED);
            System.out.println("ERROR: should have thrown!");
        } catch (Exception e) {
            System.out.println("HELD -> CONFIRMED (skipping payment) correctly rejected: " + e.getMessage());
        }
    }
    
}