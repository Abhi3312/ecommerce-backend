package com.ecommerce.backend.address.service.impl;

import com.ecommerce.backend.address.dto.AddressRequest;
import com.ecommerce.backend.address.dto.AddressResponse;
import com.ecommerce.backend.address.model.Address;
import com.ecommerce.backend.address.repository.AddressRepository;
import com.ecommerce.backend.address.service.AddressService;
import com.ecommerce.backend.common.exception.BadRequestException;
import com.ecommerce.backend.common.exception.ResourceNotFoundException;
import com.ecommerce.backend.user.model.User;
import com.ecommerce.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    public AddressResponse addAddress(String email, AddressRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.isDefault()) {
            unsetDefaultAddress(user);
        }

        Address address = new Address();
        address.setUser(user);
        mapRequestToEntity(request, address);

        return mapToResponse(addressRepository.save(address));
    }

    @Override
    public List<AddressResponse> getMyAddresses(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return addressRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AddressResponse updateAddress(Long addressId, String email, AddressRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Address does not belong to user");
        }

        if (request.isDefault()) {
            unsetDefaultAddress(user);
        }

        mapRequestToEntity(request, address);

        return mapToResponse(addressRepository.save(address));
    }

    @Override
    public void deleteAddress(Long addressId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Address does not belong to user");
        }

        addressRepository.delete(address);
    }

    // 🔹 helpers

    private void unsetDefaultAddress(User user) {
        addressRepository.findByUser(user).forEach(a -> {
            a.setDefault(false);
            addressRepository.save(a);
        });
    }

    private void mapRequestToEntity(AddressRequest request, Address address) {
        address.setFullName(request.getFullName());
        address.setPhone(request.getPhone());
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setZipCode(request.getZipCode());
        address.setCountry(request.getCountry());
        address.setDefault(request.isDefault());
    }

    private AddressResponse mapToResponse(Address address) {
        AddressResponse response = new AddressResponse();
        response.setId(address.getId());
        response.setFullName(address.getFullName());
        response.setPhone(address.getPhone());
        response.setStreet(address.getStreet());
        response.setCity(address.getCity());
        response.setState(address.getState());
        response.setZipCode(address.getZipCode());
        response.setCountry(address.getCountry());
        response.setDefault(address.isDefault());
        return response;
    }
}
