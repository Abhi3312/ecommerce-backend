package com.ecommerce.backend.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemResponse {

    private String productName;
    private BigDecimal price;
    private int quantity;
    private BigDecimal totalPrice;
}
