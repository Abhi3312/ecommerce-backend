package com.ecommerce.backend.payment.strategy.impl;

import com.ecommerce.backend.payment.model.PaymentStatus;
import com.ecommerce.backend.payment.strategy.PaymentStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PayPalPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentStatus pay(BigDecimal amount) {
        // rule: always succeed
        return PaymentStatus.SUCCESS;
    }
}


