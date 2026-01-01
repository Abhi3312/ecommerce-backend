package com.ecommerce.backend.payment.strategy;

import com.ecommerce.backend.payment.model.PaymentStatus;

import java.math.BigDecimal;

public interface PaymentStrategy {
    PaymentStatus pay(BigDecimal amount);
}
