package com.ecommerce.backend.review.service;

import com.ecommerce.backend.review.dto.ReviewRequestDTO;
import com.ecommerce.backend.review.dto.ReviewResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

    void addReview(String userEmail, Long productId, ReviewRequestDTO dto);

    void updateReview(String userEmail, Long reviewId, ReviewRequestDTO dto);

    void deleteReview(String userEmail, Long reviewId);

    Page<ReviewResponseDTO> getProductReviews(Long productId, Pageable pageable);
}
