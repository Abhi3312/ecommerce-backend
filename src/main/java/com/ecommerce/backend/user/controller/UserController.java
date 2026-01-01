package com.ecommerce.backend.user.controller;

import com.ecommerce.backend.common.response.ApiResponse;
import com.ecommerce.backend.user.dto.UpdateProfileRequest;
import com.ecommerce.backend.user.dto.UserProfileResponse;
import com.ecommerce.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
public class UserController {

    private final UserService userService;

    // VIEW PROFILE
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getMyProfile(
            Authentication authentication
    ) {
        UserProfileResponse response =
                userService.getMyProfile(authentication.getName());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Profile fetched successfully", response)
        );
    }

    //  UPDATE PROFILE
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateMyProfile(
            @RequestBody UpdateProfileRequest request,
            Authentication authentication
    ) {
        UserProfileResponse response =
                userService.updateMyProfile(authentication.getName(), request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Profile updated successfully", response)
        );
    }
}
