package com.ecommerce.backend.order.dto;

import com.ecommerce.backend.order.model.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderStatusRequest {
    private OrderStatus status;
}
