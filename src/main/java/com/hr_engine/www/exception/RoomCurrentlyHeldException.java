package com.hr_engine.www.exception;

public class RoomCurrentlyHeldException extends RuntimeException {
    public RoomCurrentlyHeldException(String message) {
        super(message);
    }
    
    
}