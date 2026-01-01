package com.ecommerce.backend.user.controller;


import com.ecommerce.backend.common.response.ApiResponse;
import com.ecommerce.backend.security.JwtUtil;
import com.ecommerce.backend.user.dto.LoginRequest;
import com.ecommerce.backend.user.dto.LoginResponse;
import com.ecommerce.backend.user.dto.RegisterRequest;
import com.ecommerce.backend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest request) {

        userService.register(request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "User registered successfully", null)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest request) {

        String token = userService.login(request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Login successful", token)
        );
    }

}
