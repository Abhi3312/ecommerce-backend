package com.ecommerce.backend.address.repository;

import com.ecommerce.backend.address.model.Address;
import com.ecommerce.backend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUser(User user);
}
