package com.ecommerce.backend.review.service.impl;

import com.ecommerce.backend.common.exception.BadRequestException;
import com.ecommerce.backend.common.exception.ResourceNotFoundException;
import com.ecommerce.backend.order.repository.OrderRepository;
import com.ecommerce.backend.product.model.Product;
import com.ecommerce.backend.product.repository.ProductRepository;
import com.ecommerce.backend.review.dto.ReviewRequestDTO;
import com.ecommerce.backend.review.dto.ReviewResponseDTO;
import com.ecommerce.backend.review.model.Review;
import com.ecommerce.backend.review.repository.ReviewRepository;
import com.ecommerce.backend.review.service.ReviewService;
import com.ecommerce.backend.user.model.User;
import com.ecommerce.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public void addReview(String userEmail, Long productId, ReviewRequestDTO dto) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        boolean hasPurchased =
                orderRepository.existsByUserIdAndItemsProductId(
                        user.getId(), productId
                );

        if (!hasPurchased) {
            throw new BadRequestException("You can review only purchased products");
        }

        if (reviewRepository.existsByUserIdAndProductId(user.getId(), productId)) {
            throw new BadRequestException("You have already reviewed this product");
        }

        validateRating(dto.getRating());

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setCreatedAt(LocalDateTime.now());

        reviewRepository.save(review);

        updateProductRating(productId);
    }

    @Override
    public void updateReview(String userEmail, Long reviewId, ReviewRequestDTO dto) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!review.getUser().getEmail().equals(userEmail)) {
            throw new BadRequestException("You can update only your own review");
        }

        validateRating(dto.getRating());

        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        reviewRepository.save(review);

        updateProductRating(review.getProduct().getId());
    }

    @Override
    public void deleteReview(String userEmail, Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!review.getUser().getEmail().equals(userEmail)) {
            throw new BadRequestException("You can delete only your own review");
        }

        Long productId = review.getProduct().getId();

        reviewRepository.delete(review);

        updateProductRating(productId);
    }

    @Override
    public Page<ReviewResponseDTO> getProductReviews(
            Long productId, Pageable pageable) {

        return reviewRepository.findByProductId(productId, pageable)
                .map(review -> {
                    ReviewResponseDTO dto = new ReviewResponseDTO();
                    dto.setId(review.getId());
                    dto.setRating(review.getRating());
                    dto.setComment(review.getComment());
                    dto.setUserName(review.getUser().getName());
                    dto.setCreatedAt(review.getCreatedAt());
                    return dto;
                });
    }

    private void validateRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new BadRequestException("Rating must be between 1 and 5");
        }
    }

    private void updateProductRating(Long productId) {

        Double avgRating = reviewRepository.findAverageRating(productId);
        long count = reviewRepository.countByProductId(productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setAverageRating(avgRating == null ? 0.0 : avgRating);
        product.setTotalReviews((int) count);

        productRepository.save(product);
    }
}
