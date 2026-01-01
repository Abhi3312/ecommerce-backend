package com.ecommerce.backend.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class TopProductResponse {

    private String productName;
    private long quantitySold;
    private BigDecimal revenue;
}
