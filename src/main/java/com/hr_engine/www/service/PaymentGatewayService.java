package com.hr_engine.www.service;

import com.hr_engine.www.dto.PaymentRequestDto;
import com.hr_engine.www.dto.PaymentResult;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class PaymentGatewayService {

    /**
     * Simulates calling a real third-party payment gateway.
     * In production, this would be replaced with an actual HTTP call
     * to Stripe, Razorpay, etc.
     */
    public PaymentResult processPayment(PaymentRequestDto request, double amount) {
        // Simulate realistic network latency for a payment call
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (request.isSimulateFailure()) {
            return new PaymentResult(
                    false,
                    null,
                    "Payment declined - insufficient funds (simulated failure)"
            );
        }

        String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new PaymentResult(
                true,
                transactionId,
                "Payment of ₹" + amount + " processed successfully"
        );
    }
    
}