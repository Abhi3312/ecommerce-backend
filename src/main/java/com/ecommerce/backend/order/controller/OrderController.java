package com.ecommerce.backend.order.controller;

import com.ecommerce.backend.common.response.ApiResponse;
import com.ecommerce.backend.order.dto.OrderResponse;
import com.ecommerce.backend.order.dto.PlaceOrderRequest;
import com.ecommerce.backend.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(
            @RequestBody PlaceOrderRequest request,
            Authentication authentication
    ) {
        OrderResponse response =
                orderService.placeOrder(authentication.getName(), request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Order placed successfully", response)
        );
    }


    @GetMapping("/my")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getMyOrders(
            Pageable pageable,
            Authentication authentication
    ) {

        Page<OrderResponse> response =
                orderService.getMyOrders(authentication.getName(), pageable);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Orders fetched successfully", response)
        );
    }
}
