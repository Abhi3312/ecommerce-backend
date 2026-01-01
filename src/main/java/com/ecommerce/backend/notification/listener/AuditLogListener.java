package com.ecommerce.backend.notification.listener;

import com.ecommerce.backend.order.event.OrderStatusChangedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AuditLogListener {

    @EventListener
    public void logOrderStatusChange(OrderStatusChangedEvent event) {

        System.out.println(
                "Audit Log: Order "
                        + event.getOrder().getId()
                        + " status changed to "
                        + event.getOrder().getStatus()
        );
    }
}
