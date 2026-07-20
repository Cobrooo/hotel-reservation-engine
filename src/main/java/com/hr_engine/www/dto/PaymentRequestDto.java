package com.hr_engine.www.dto;

public class PaymentRequestDto {
    private boolean simulateFailure; // lets you deterministically force success/failure in testing
    private String cardNumber; // mock only - never actually validated or stored meaningfully

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