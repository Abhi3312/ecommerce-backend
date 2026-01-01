package com.ecommerce.backend.order.service;

import com.ecommerce.backend.order.dto.OrderResponse;
import com.ecommerce.backend.order.dto.PlaceOrderRequest;
import com.ecommerce.backend.order.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderResponse placeOrder(String email, PlaceOrderRequest request);

    Page<OrderResponse> getMyOrders(String email, Pageable pageable);

    Page<OrderResponse> getAllOrders(Pageable pageable);

    OrderResponse updateOrderStatus(Long orderId, OrderStatus status);
}
