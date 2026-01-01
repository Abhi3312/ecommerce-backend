package com.ecommerce.backend.cart.repository;

import com.ecommerce.backend.cart.model.Cart;
import com.ecommerce.backend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}

