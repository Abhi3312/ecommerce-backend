package com.ecommerce.backend.payment.service;

import com.ecommerce.backend.payment.dto.PaymentRequest;
import com.ecommerce.backend.payment.dto.PaymentResponse;

public interface PaymentService {
    PaymentResponse pay(PaymentRequest request, String email);
}
