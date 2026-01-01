package com.ecommerce.backend.notification.listener;


import com.ecommerce.backend.order.event.OrderStatusChangedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationListener {

    @EventListener
    public void handleOrderStatusChange(OrderStatusChangedEvent event) {

        System.out.println(
                "Email sent to customer for Order ID: "
                        + event.getOrder().getId()
                        + " | Status: "
                        + event.getOrder().getStatus()
        );
    }
}

