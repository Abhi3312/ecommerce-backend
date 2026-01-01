package com.ecommerce.backend.payment.strategy.impl;

import com.ecommerce.backend.payment.model.PaymentStatus;
import com.ecommerce.backend.payment.strategy.PaymentStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CreditCardPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentStatus pay(BigDecimal amount) {
        // rule: succeed if amount <= 100000
        return amount.compareTo(BigDecimal.valueOf(100000)) <= 0
                ? PaymentStatus.SUCCESS
                : PaymentStatus.FAILED;
    }
}
