package com.ecommerce.backend.cart.service;

import com.ecommerce.backend.cart.dto.AddToCartRequest;
import com.ecommerce.backend.cart.dto.CartResponse;
import com.ecommerce.backend.cart.dto.UpdateCartRequest;

public interface CartService {
    CartResponse addToCart(String email, AddToCartRequest request);

    CartResponse updateQuantity(String email, UpdateCartRequest request);

    CartResponse removeItem(String email, Long productId);

    CartResponse viewCart(String email);

    CartResponse clearCart(String email);
}
