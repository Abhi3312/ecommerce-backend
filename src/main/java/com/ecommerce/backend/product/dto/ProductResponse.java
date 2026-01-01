package com.ecommerce.backend.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private String category;

    // Reviews data
    private double averageRating;
    private int totalReviews;
}
