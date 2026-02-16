package com.ecommerce.backend.product.service.impl;

import com.ecommerce.backend.common.exception.ResourceNotFoundException;
import com.ecommerce.backend.product.dto.ProductRequest;
import com.ecommerce.backend.product.dto.ProductResponse;
import com.ecommerce.backend.product.model.Product;
import com.ecommerce.backend.product.repository.ProductRepository;
import com.ecommerce.backend.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(ProductRequest request) {

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .totalStock(request.getTotalStock())
                .availableStock(request.getTotalStock())
                .reservedStock(0)
                .category(request.getCategory())
                .build();

        return mapToResponse(productRepository.save(product));
    }

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + productId)
                );

        return mapToResponse(product);
    }

    @Override
    public Page<ProductResponse> getProductsByCategory(
            String category,
            Pageable pageable
    ) {
        return productRepository.findByCategory(category, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<ProductResponse> getProductsByPriceRange(
            BigDecimal min,
            BigDecimal max,
            Pageable pageable
    ) {
        return productRepository.findByPriceBetween(min, max, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public ProductResponse updateProduct(Long productId, ProductRequest request) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + productId)
                );

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory());

        int newTotalStock = request.getTotalStock();
        int diff = newTotalStock - product.getTotalStock();

        product.setTotalStock(newTotalStock);
        product.setAvailableStock(
                product.getAvailableStock() + diff
        );


        return mapToResponse(productRepository.save(product));
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + productId)
                );

        productRepository.delete(product);
    }

    // CENTRALIZED MAPPING LOGIC
    private ProductResponse mapToResponse(Product product) {

        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setTotalStock(product.getTotalStock());
        response.setAvailableStock(product.getAvailableStock());
        response.setReservedStock(product.getReservedStock());
        response.setCategory(product.getCategory());

        // Reviews data (from Product snapshot)
        response.setAverageRating(product.getAverageRating());
        response.setTotalReviews(product.getTotalReviews());

        return response;
    }
}
