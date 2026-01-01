package com.ecommerce.backend.review.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewResponseDTO {
    private Long id;
    private int rating;
    private String comment;
    private String userName;
    private LocalDateTime createdAt;
}
