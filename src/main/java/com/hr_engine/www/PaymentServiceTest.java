package com.hr_engine.www;

import com.hr_engine.www.dto.PaymentRequestDto;
import com.hr_engine.www.dto.PaymentResult;
import com.hr_engine.www.service.PaymentGatewayService;

public class PaymentServiceTest {
    public static void main(String[] args) {
        PaymentGatewayService service = new PaymentGatewayService();

        PaymentRequestDto successRequest = new PaymentRequestDto();
        successRequest.setSimulateFailure(false);
        successRequest.setCardNumber("4111111111111111");

        PaymentResult successResult = service.processPayment(successRequest, 7500.00);
        System.out.println("Success case -> success: " + successResult.isSuccess()
                + " | txnId: " + successResult.getTransactionId()
                + " | message: " + successResult.getMessage());

        PaymentRequestDto failRequest = new PaymentRequestDto();
        failRequest.setSimulateFailure(true);

        PaymentResult failResult = service.processPayment(failRequest, 7500.00);
        System.out.println("Failure case -> success: " + failResult.isSuccess()
                + " | txnId: " + failResult.getTransactionId()
                + " | message: " + failResult.getMessage());
    }
    
}