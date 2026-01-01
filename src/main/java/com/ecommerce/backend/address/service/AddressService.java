package com.ecommerce.backend.address.service;

import com.ecommerce.backend.address.dto.AddressRequest;
import com.ecommerce.backend.address.dto.AddressResponse;

import java.util.List;

public interface AddressService {

    AddressResponse addAddress(String email, AddressRequest request);

    List<AddressResponse> getMyAddresses(String email);

    AddressResponse updateAddress(Long addressId, String email, AddressRequest request);

    void deleteAddress(Long addressId, String email);
}
