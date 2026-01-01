package com.ecommerce.backend.order.event;

import com.ecommerce.backend.order.model.Order;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrderStatusChangedEvent extends ApplicationEvent {

    private final Order order;

    public OrderStatusChangedEvent(Object source, Order order) {
        super(source);
        this.order = order;
    }
}
