package com.ecommerce.backend.address.controller;

import com.ecommerce.backend.address.dto.AddressRequest;
import com.ecommerce.backend.address.dto.AddressResponse;
import com.ecommerce.backend.address.service.AddressService;
import com.ecommerce.backend.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponse>> addAddress(
            @RequestBody AddressRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Address added successfully",
                        addressService.addAddress(authentication.getName(), request)
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getMyAddresses(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Addresses fetched successfully",
                        addressService.getMyAddresses(authentication.getName())
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(
            @PathVariable Long id,
            @RequestBody AddressRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Address updated successfully",
                        addressService.updateAddress(id, authentication.getName(), request)
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @PathVariable Long id,
            Authentication authentication
    ) {
        addressService.deleteAddress(id, authentication.getName());
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Address deleted successfully", null)
        );
    }
}


