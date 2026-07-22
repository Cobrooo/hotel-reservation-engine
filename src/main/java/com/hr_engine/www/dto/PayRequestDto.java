package com.hr_engine.www.dto;

public class PayRequestDto {
    private boolean simulateFailure;
    private String cardNumber;

    public boolean isSimulateFailure() { 
    	return simulateFailure; 
    }
    
    public void setSimulateFailure(boolean simulateFailure) { 
    	this.simulateFailure = simulateFailure; 
    }

    public String getCardNumber() { 
    	return cardNumber; 
    }
    
    public void setCardNumber(String cardNumber) { 
    	this.cardNumber = cardNumber; 
    }
    
}