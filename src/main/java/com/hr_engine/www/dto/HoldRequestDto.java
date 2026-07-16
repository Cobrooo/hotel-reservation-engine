package com.hr_engine.www.dto;

import java.time.LocalDate;

public class HoldRequestDto {
    private Long roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String guestName;
    private String guestEmail;

    // Getters and setters
    public Long getRoomId() { 
    	return roomId; 
    }
    
    public void setRoomId(Long roomId) { 
    	this.roomId = roomId; 
    }

    public LocalDate getCheckInDate() { 
    	return checkInDate; 
    }
    
    public void setCheckInDate(LocalDate checkInDate) { 
    	this.checkInDate = checkInDate; 
    }

    public LocalDate getCheckOutDate() { 
    	return checkOutDate; 
    }
    
    public void setCheckOutDate(LocalDate checkOutDate) { 
    	this.checkOutDate = checkOutDate; 
    }

    public String getGuestName() { 
    	return guestName; 
    }
    
    public void setGuestName(String guestName) { 
    	this.guestName = guestName; 
    }

    public String getGuestEmail() { 
    	return guestEmail; 
    }
    
    public void setGuestEmail(String guestEmail) { 
    	this.guestEmail = guestEmail; 
    }
    
}