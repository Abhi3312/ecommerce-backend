package com.ecommerce.backend.order.service.impl;

import com.ecommerce.backend.address.model.Address;
import com.ecommerce.backend.address.repository.AddressRepository;
import com.ecommerce.backend.cart.model.Cart;
import com.ecommerce.backend.cart.model.CartItem;
import com.ecommerce.backend.cart.repository.CartRepository;
import com.ecommerce.backend.common.exception.BadRequestException;
import com.ecommerce.backend.common.exception.ResourceNotFoundException;
import com.ecommerce.backend.order.dto.OrderItemResponse;
import com.ecommerce.backend.order.dto.OrderResponse;
import com.ecommerce.backend.order.dto.PlaceOrderRequest;
import com.ecommerce.backend.order.model.Order;
import com.ecommerce.backend.order.model.OrderItem;
import com.ecommerce.backend.order.model.OrderStatus;
import com.ecommerce.backend.order.repository.OrderRepository;
import com.ecommerce.backend.order.service.OrderService;
import com.ecommerce.backend.product.model.Product;
import com.ecommerce.backend.user.model.User;
import com.ecommerce.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;



    @Transactional
    @Override
    public OrderResponse placeOrder(String email, PlaceOrderRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Address does not belong to user");
        }

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new BadRequestException("Cart is empty"));

        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PLACED);

        order.setShippingFullName(address.getFullName());
        order.setShippingPhone(address.getPhone());
        order.setShippingStreet(address.getStreet());
        order.setShippingCity(address.getCity());
        order.setShippingState(address.getState());
        order.setShippingZipCode(address.getZipCode());
        order.setShippingCountry(address.getCountry());

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {

            Product product = cartItem.getProduct();

            if (cartItem.getQuantity() > product.getAvailableStock()) {
                throw new BadRequestException(
                        "Insufficient stock for " + product.getName()
                );
            }


            product.setAvailableStock(
                    product.getAvailableStock() - cartItem.getQuantity()
            );

            product.setReservedStock(
                    product.getReservedStock() + cartItem.getQuantity()
            );


            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());

            order.getItems().add(orderItem);

            totalAmount = totalAmount.add(
                    product.getPrice()
                            .multiply(BigDecimal.valueOf(cartItem.getQuantity()))
            );
        }

        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        return mapToOrderResponse(savedOrder);
    }



    @Override
    public Page<OrderResponse> getMyOrders(String email, Pageable pageable) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Page<Order> ordersPage =
                orderRepository.findByUser(user, pageable);

        return ordersPage.map(this::mapToOrderResponse);
    }


    @Override
    public Page<OrderResponse> getAllOrders(Pageable pageable) {

        Page<Order> orders = orderRepository.findAll(pageable);
        return orders.map(this::mapToOrderResponse);
    }

    @Transactional
    @Override
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus newStatus) {

        //  Admin is allowed to update only fulfilment statuses
        if (newStatus != OrderStatus.SHIPPED &&
                newStatus != OrderStatus.DELIVERED) {
            throw new BadRequestException(
                    "Admin can update order status only to SHIPPED or DELIVERED"
            );
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        OrderStatus currentStatus = order.getStatus();

        // Delivered orders are immutable
        if (currentStatus == OrderStatus.DELIVERED) {
            throw new BadRequestException(
                    "Delivered order status cannot be changed"
            );
        }

        // PAID → SHIPPED is the only valid way to SHIP
        if (newStatus == OrderStatus.SHIPPED &&
                currentStatus != OrderStatus.PAID) {
            throw new BadRequestException(
                    "Order must be PAID before it can be SHIPPED"
            );
        }

        // SHIPPED → DELIVERED is the only valid way to DELIVER
        if (newStatus == OrderStatus.DELIVERED &&
                currentStatus != OrderStatus.SHIPPED) {
            throw new BadRequestException(
                    "Order must be SHIPPED before it can be DELIVERED"
            );
        }

        order.setStatus(newStatus);

        return mapToOrderResponse(orderRepository.save(order));
    }

    
    private OrderResponse mapToOrderResponse(Order order) {

        List<OrderItemResponse> items = new ArrayList<>();

        for (OrderItem item : order.getItems()) {
            OrderItemResponse itemResponse = new OrderItemResponse();
            itemResponse.setProductName(item.getProductName());
            itemResponse.setPrice(item.getPrice());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setTotalPrice(
                    item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
            );
            items.add(itemResponse);
        }

        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getId());
        response.setStatus(order.getStatus());
        response.setTotalAmount(order.getTotalAmount());
        response.setCreatedAt(order.getCreatedAt());
        response.setItems(items);

        // ADDRESS SNAPSHOT MAPPING
        response.setShippingFullName(order.getShippingFullName());
        response.setShippingPhone(order.getShippingPhone());
        response.setShippingStreet(order.getShippingStreet());
        response.setShippingCity(order.getShippingCity());
        response.setShippingState(order.getShippingState());
        response.setShippingZipCode(order.getShippingZipCode());
        response.setShippingCountry(order.getShippingCountry());

        return response;
    }

}
