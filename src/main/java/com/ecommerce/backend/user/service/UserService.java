package com.ecommerce.backend.user.service;

import com.ecommerce.backend.user.dto.LoginRequest;
import com.ecommerce.backend.user.dto.RegisterRequest;
import com.ecommerce.backend.user.dto.UpdateProfileRequest;
import com.ecommerce.backend.user.dto.UserProfileResponse;

public interface UserService {

    void register(RegisterRequest request);

    String login(LoginRequest request);

    UserProfileResponse getMyProfile(String email);

    UserProfileResponse updateMyProfile(String email, UpdateProfileRequest request);
}
