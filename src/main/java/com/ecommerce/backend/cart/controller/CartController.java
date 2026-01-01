package com.ecommerce.backend.cart.controller;

import com.ecommerce.backend.cart.dto.AddToCartRequest;
import com.ecommerce.backend.cart.dto.CartResponse;
import com.ecommerce.backend.cart.dto.UpdateCartRequest;
import com.ecommerce.backend.cart.service.CartService;
import com.ecommerce.backend.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<CartResponse>> addToCart(
            @RequestBody AddToCartRequest request,
            Authentication authentication
    ) {

        CartResponse response =
                cartService.addToCart(authentication.getName(), request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Product added to cart", response)
        );
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<CartResponse>> updateQuantity(
            @RequestBody UpdateCartRequest request,
            Authentication authentication
    ) {
        CartResponse response =
                cartService.updateQuantity(authentication.getName(), request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Cart updated", response)
        );
    }


    @DeleteMapping("/remove/{productId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<CartResponse>> removeItem(
            @PathVariable Long productId,
            Authentication authentication
    ) {
        CartResponse response =
                cartService.removeItem(authentication.getName(), productId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Item removed from cart", response)
        );
    }


    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<CartResponse>> viewCart(
            Authentication authentication
    ) {

        CartResponse response =
                cartService.viewCart(authentication.getName());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Cart fetched successfully", response)
        );
    }

    @DeleteMapping("/clear")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<CartResponse>> clearCart(
            Authentication authentication
    ) {

        CartResponse response =
                cartService.clearCart(authentication.getName());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Cart cleared successfully", response)
        );
    }
}

