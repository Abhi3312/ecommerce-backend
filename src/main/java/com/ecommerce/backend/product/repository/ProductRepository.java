
package com.ecommerce.backend.product.repository;

import com.ecommerce.backend.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByCategory(String category, Pageable pageable);

    Page<Product> findByPriceBetween(
            BigDecimal min,
            BigDecimal max,
            Pageable pageable
    );
}

