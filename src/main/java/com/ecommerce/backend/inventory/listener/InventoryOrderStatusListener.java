package com.ecommerce.backend.inventory.listener;

import com.ecommerce.backend.order.event.OrderStatusChangedEvent;
import com.ecommerce.backend.order.model.Order;
import com.ecommerce.backend.order.model.OrderItem;
import com.ecommerce.backend.order.model.OrderStatus;
import com.ecommerce.backend.product.model.Product;
import com.ecommerce.backend.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InventoryOrderStatusListener {

    private final ProductRepository productRepository;

    @EventListener
    @Transactional
    public void handleOrderStatusChange(OrderStatusChangedEvent event) {

        Order order = event.getOrder();

        for (OrderItem item : order.getItems()) {

            Product product = productRepository
                    .findById(item.getProductId())
                    .orElseThrow();


            if (order.getStatus() == OrderStatus.PAID) {

                product.setReservedStock(
                        product.getReservedStock() - item.getQuantity()
                );

                product.setTotalStock(
                        product.getTotalStock() - item.getQuantity()
                );
            }

            
            if (order.getStatus() == OrderStatus.CANCELLED) {

                product.setReservedStock(
                        product.getReservedStock() - item.getQuantity()
                );

                product.setAvailableStock(
                        product.getAvailableStock() + item.getQuantity()
                );
            }
        }
    }
}
