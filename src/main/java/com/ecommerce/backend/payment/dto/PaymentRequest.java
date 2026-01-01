package com.ecommerce.backend.payment.dto;

import com.ecommerce.backend.payment.model.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    private Long orderId;
    private PaymentMethod method;
}

