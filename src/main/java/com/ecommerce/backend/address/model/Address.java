package com.ecommerce.backend.address.model;

import com.ecommerce.backend.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "addresses")
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    private String fullName;
    private String phone;

    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    private boolean isDefault;
}
