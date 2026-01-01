package com.ecommerce.backend.payment.dto;

import com.ecommerce.backend.payment.model.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponse {
    private Long paymentId;
    private PaymentStatus status;
    private String message;
}
