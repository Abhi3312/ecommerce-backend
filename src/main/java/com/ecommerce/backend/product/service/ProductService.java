package com.ecommerce.backend.product.service;

import com.ecommerce.backend.product.dto.ProductRequest;
import com.ecommerce.backend.product.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    Page<ProductResponse> getAllProducts(Pageable pageable);

    ProductResponse getProductById(Long productId);

    Page<ProductResponse> getProductsByCategory(String category, Pageable pageable);

    Page<ProductResponse> getProductsByPriceRange(
            BigDecimal min,
            BigDecimal max,
            Pageable pageable
    );

    ProductResponse updateProduct(Long productId, ProductRequest request);

    void deleteProduct(Long productId);
}
