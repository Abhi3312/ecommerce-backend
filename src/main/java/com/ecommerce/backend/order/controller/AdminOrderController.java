package com.ecommerce.backend.order.controller;

import com.ecommerce.backend.common.response.ApiResponse;
import com.ecommerce.backend.order.dto.OrderResponse;
import com.ecommerce.backend.order.dto.UpdateOrderStatusRequest;
import com.ecommerce.backend.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getAllOrders(
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "All orders fetched successfully",
                        orderService.getAllOrders(pageable)
                )
        );
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateOrderStatusRequest request
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Order status updated successfully",
                        orderService.updateOrderStatus(orderId, request.getStatus())
                )
        );
    }
}
