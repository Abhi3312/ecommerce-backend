package com.ecommerce.backend.cart.service.impl;

import com.ecommerce.backend.cart.dto.AddToCartRequest;
import com.ecommerce.backend.cart.dto.CartItemResponse;
import com.ecommerce.backend.cart.dto.CartResponse;
import com.ecommerce.backend.cart.dto.UpdateCartRequest;
import com.ecommerce.backend.cart.model.Cart;
import com.ecommerce.backend.cart.model.CartItem;
import com.ecommerce.backend.cart.repository.CartRepository;
import com.ecommerce.backend.cart.service.CartService;
import com.ecommerce.backend.common.exception.BadRequestException;
import com.ecommerce.backend.common.exception.ResourceNotFoundException;
import com.ecommerce.backend.product.model.Product;
import com.ecommerce.backend.product.repository.ProductRepository;
import com.ecommerce.backend.user.model.User;
import com.ecommerce.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public CartResponse addToCart(String email, AddToCartRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (request.getQuantity() <= 0) {
            throw new BadRequestException("Quantity must be at least 1");
        }

        if (request.getQuantity() > product.getStock()) {
            throw new BadRequestException("Insufficient stock");
        }

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return newCart;
                });

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQty = item.getQuantity() + request.getQuantity();

            if (newQty > product.getStock()) {
                throw new BadRequestException("Insufficient stock");
            }

            item.setQuantity(newQty);
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(request.getQuantity());
            cart.getItems().add(item);
        }

        return mapToCartResponse(cartRepository.save(cart));
    }

    @Override
    public CartResponse updateQuantity(String email, UpdateCartRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(request.getProductId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not in cart"));

        if (request.getQuantity() <= 0) {
            throw new BadRequestException("Quantity must be at least 1");
        }

        if (request.getQuantity() > cartItem.getProduct().getStock()) {
            throw new BadRequestException("Insufficient stock");
        }

        cartItem.setQuantity(request.getQuantity());

        return mapToCartResponse(cartRepository.save(cart));
    }

    @Override
    public CartResponse removeItem(String email, Long productId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        boolean removed = cart.getItems().removeIf(
                item -> item.getProduct().getId().equals(productId)
        );

        if (!removed) {
            throw new ResourceNotFoundException("Product not in cart");
        }

        return mapToCartResponse(cartRepository.save(cart));
    }

    @Override
    public CartResponse viewCart(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return cartRepository.findByUser(user)
                .map(this::mapToCartResponse)
                .orElseGet(() -> {
                    CartResponse response = new CartResponse();
                    response.setItems(Collections.emptyList());
                    response.setTotalAmount(BigDecimal.ZERO);
                    return response;
                });
    }

    @Override
    public CartResponse clearCart(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Optional<Cart> cartOpt = cartRepository.findByUser(user);

        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            cart.getItems().clear(); // orphanRemoval = true
            cartRepository.save(cart);
        }

        CartResponse response = new CartResponse();
        response.setItems(Collections.emptyList());
        response.setTotalAmount(BigDecimal.ZERO);
        return response;
    }

    private CartResponse mapToCartResponse(Cart cart) {

        List<CartItemResponse> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cart.getItems()) {
            BigDecimal itemTotal =
                    item.getProduct().getPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()));

            CartItemResponse response = new CartItemResponse();
            response.setProductId(item.getProduct().getId());
            response.setProductName(item.getProduct().getName());
            response.setPrice(item.getProduct().getPrice());
            response.setQuantity(item.getQuantity());
            response.setTotalPrice(itemTotal);

            items.add(response);
            total = total.add(itemTotal);
        }

        CartResponse cartResponse = new CartResponse();
        cartResponse.setItems(items);
        cartResponse.setTotalAmount(total);
        return cartResponse;
    }
}


