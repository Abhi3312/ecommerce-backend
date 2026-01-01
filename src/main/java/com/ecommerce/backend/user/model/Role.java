package com.ecommerce.backend.user.model;

/**
 * Role defines the type of user in the system.
 * ADMIN  -> Can manage products, orders, reports
 * CUSTOMER -> Can browse, order, review products
 */
public enum Role {
    ADMIN,
    CUSTOMER
}
