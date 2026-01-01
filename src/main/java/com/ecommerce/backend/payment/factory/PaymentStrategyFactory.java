package com.ecommerce.backend.payment.factory;

import com.ecommerce.backend.payment.model.PaymentMethod;
import com.ecommerce.backend.payment.strategy.PaymentStrategy;
import com.ecommerce.backend.payment.strategy.impl.CreditCardPaymentStrategy;
import com.ecommerce.backend.payment.strategy.impl.PayPalPaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentStrategyFactory {

    private final CreditCardPaymentStrategy creditCard;
    private final PayPalPaymentStrategy payPal;

    public PaymentStrategy getStrategy(PaymentMethod method) {
        return switch (method) {
            case CREDIT_CARD -> creditCard;
            case PAYPAL -> payPal;
        };
    }
}
