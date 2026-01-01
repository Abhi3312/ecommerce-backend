package com.ecommerce.backend.user.service.impl;

import com.ecommerce.backend.common.exception.BadRequestException;
import com.ecommerce.backend.common.exception.ResourceNotFoundException;
import com.ecommerce.backend.security.JwtUtil;
import com.ecommerce.backend.user.dto.LoginRequest;
import com.ecommerce.backend.user.dto.RegisterRequest;
import com.ecommerce.backend.user.dto.UpdateProfileRequest;
import com.ecommerce.backend.user.dto.UserProfileResponse;
import com.ecommerce.backend.user.model.Role;
import com.ecommerce.backend.user.model.User;
import com.ecommerce.backend.user.repository.UserRepository;
import com.ecommerce.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public void register(RegisterRequest request) {

        // check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        // 2. Create user entity
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CUSTOMER)
                .build();

        // 3. Save user to database
        userRepository.save(user);


    }


    @Override
    public String login(LoginRequest request) {

        //  Spring Security se authentication karwao
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        //  Agar yaha tak aa gaya → password correct hai
        // JWT generate karo
        return jwtUtil.generateToken(request.getEmail());
    }

    @Override
    public UserProfileResponse getMyProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return mapToResponse(user);
    }

    @Override
    public UserProfileResponse updateMyProfile(String email, UpdateProfileRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getName() == null || request.getName().isBlank()) {
            throw new BadRequestException("Name cannot be empty");
        }

        user.setName(request.getName());

        User updatedUser = userRepository.save(user);

        return mapToResponse(updatedUser);
    }

    // Mapper
    private UserProfileResponse mapToResponse(User user) {

        UserProfileResponse response = new UserProfileResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());

        return response;
    }





}
