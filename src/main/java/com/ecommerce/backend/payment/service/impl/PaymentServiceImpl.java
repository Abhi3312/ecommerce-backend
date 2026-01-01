package com.ecommerce.backend.payment.service.impl;

import com.ecommerce.backend.order.event.OrderStatusChangedEvent;
import org.springframework.context.ApplicationEventPublisher;
import com.ecommerce.backend.common.exception.BadRequestException;
import com.ecommerce.backend.common.exception.ResourceNotFoundException;
import com.ecommerce.backend.order.model.Order;
import com.ecommerce.backend.order.model.OrderStatus;
import com.ecommerce.backend.order.repository.OrderRepository;
import com.ecommerce.backend.payment.dto.PaymentRequest;
import com.ecommerce.backend.payment.dto.PaymentResponse;
import com.ecommerce.backend.payment.factory.PaymentStrategyFactory;
import com.ecommerce.backend.payment.model.Payment;
import com.ecommerce.backend.payment.model.PaymentStatus;
import com.ecommerce.backend.payment.repository.PaymentRepository;
import com.ecommerce.backend.payment.service.PaymentService;
import com.ecommerce.backend.payment.strategy.PaymentStrategy;
import com.ecommerce.backend.user.model.User;
import com.ecommerce.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PaymentStrategyFactory factory;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public PaymentResponse pay(PaymentRequest request, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Order does not belong to user");
        }

        if (order.getStatus() != OrderStatus.PLACED) {
            throw new BadRequestException("Order is not payable");
        }

        PaymentStrategy strategy = factory.getStrategy(request.getMethod());
        PaymentStatus result = strategy.pay(order.getTotalAmount());

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setMethod(request.getMethod());
        payment.setAmount(order.getTotalAmount());
        payment.setStatus(result);

        paymentRepository.save(payment);

        // Update order status based on payment result
        order.setStatus(result == PaymentStatus.SUCCESS
                ? OrderStatus.PAID
                : OrderStatus.CANCELLED);

        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(payment.getId());
        response.setStatus(result);
        response.setMessage(
                result == PaymentStatus.SUCCESS
                        ? "Payment successful"
                        : "Payment failed"
        );

        eventPublisher.publishEvent(
                new OrderStatusChangedEvent(this, order)
        );


        return response;
    }
}
