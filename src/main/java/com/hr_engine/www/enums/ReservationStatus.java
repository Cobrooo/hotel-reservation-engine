package com.hr_engine.www.enums;

public enum ReservationStatus {

	PENDING,
    CONFIRMED,
    CANCELLED,
    HELD,               // new - room is locked, awaiting payment
    PAYMENT_PROCESSING, // new - payment in progress
    RELEASED            // new - hold expired/cancelled, room freed
	
}
