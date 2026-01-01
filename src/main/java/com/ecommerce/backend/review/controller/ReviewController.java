package com.ecommerce.backend.review.controller;

import com.ecommerce.backend.common.response.ApiResponse;
import com.ecommerce.backend.review.dto.ReviewRequestDTO;
import com.ecommerce.backend.review.dto.ReviewResponseDTO;
import com.ecommerce.backend.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{productId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<Void>> addReview(
            @PathVariable Long productId,
            @RequestBody ReviewRequestDTO dto,
            Authentication authentication
    ) {

        reviewService.addReview(authentication.getName(), productId, dto);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Review added successfully", null)
        );
    }

    @PutMapping("/{reviewId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<Void>> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewRequestDTO dto,
            Authentication authentication
    ) {

        reviewService.updateReview(authentication.getName(), reviewId, dto);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Review updated successfully", null)
        );
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @PathVariable Long reviewId,
            Authentication authentication
    ) {

        reviewService.deleteReview(authentication.getName(), reviewId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Review deleted successfully", null)
        );
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<Page<ReviewResponseDTO>>> getProductReviews(
            @PathVariable Long productId,
            Pageable pageable
    ) {

        Page<ReviewResponseDTO> response =
                reviewService.getProductReviews(productId, pageable);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Reviews fetched successfully", response)
        );
    }
}
