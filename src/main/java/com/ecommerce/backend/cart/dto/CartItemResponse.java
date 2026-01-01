package com.ecommerce.backend.cart.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartItemResponse {
    private Long productId;
    private String productName;
    private BigDecimal price;
    private int quantity;
    private BigDecimal totalPrice;
}

