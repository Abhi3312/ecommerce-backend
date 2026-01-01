package com.ecommerce.backend.payment.controller;

import com.ecommerce.backend.common.response.ApiResponse;
import com.ecommerce.backend.payment.dto.PaymentRequest;
import com.ecommerce.backend.payment.dto.PaymentResponse;
import com.ecommerce.backend.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<PaymentResponse>> pay(
            @RequestBody PaymentRequest request,
            Authentication authentication
    ) {
        PaymentResponse response =
                paymentService.pay(request, authentication.getName());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Payment processed", response)
        );
    }
}
