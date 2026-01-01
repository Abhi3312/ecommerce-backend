package com.ecommerce.backend.notification.listener;

import com.ecommerce.backend.order.event.OrderStatusChangedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AdminNotificationListener {

    @EventListener
    public void onOrderStatusChanged(OrderStatusChangedEvent event) {

        System.out.println(
                "Admin notified: Order "
                        + event.getOrder().getId()
                        + " is now "
                        + event.getOrder().getStatus()
        );
    }
}
