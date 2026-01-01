package com.ecommerce.backend.cart.repository;

import com.ecommerce.backend.cart.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}

