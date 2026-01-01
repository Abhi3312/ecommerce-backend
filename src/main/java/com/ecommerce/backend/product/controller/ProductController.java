package com.ecommerce.backend.product.controller;

import com.ecommerce.backend.common.response.ApiResponse;
import com.ecommerce.backend.product.dto.ProductRequest;
import com.ecommerce.backend.product.dto.ProductResponse;
import com.ecommerce.backend.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    // CREATE product (ADMIN)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductRequest request
    ) {

        ProductResponse product = productService.createProduct(request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Product created successfully", product)
        );
    }

    // UPDATE product (ADMIN)
    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductRequest request
    ) {

        ProductResponse updatedProduct =
                productService.updateProduct(productId, request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Product updated successfully", updatedProduct)
        );
    }

    // DELETE product (ADMIN)
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable Long productId
    ) {

        productService.deleteProduct(productId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Product deleted successfully", null)
        );
    }

    // ================= PUBLIC APIs =================

    // GET all products (pagination)
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<ProductResponse> products =
                productService.getAllProducts(pageable);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Products fetched successfully", products)
        );
    }

    // GET product by ID
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @PathVariable Long productId
    ) {

        ProductResponse product =
                productService.getProductById(productId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Product fetched successfully", product)
        );
    }

    // GET products by category
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<ProductResponse> products =
                productService.getProductsByCategory(category, pageable);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Products fetched successfully", products)
        );
    }

    // GET products by price range
    @GetMapping("/price")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getByPriceRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<ProductResponse> products =
                productService.getProductsByPriceRange(min, max, pageable);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Products fetched successfully", products)
        );
    }
}
