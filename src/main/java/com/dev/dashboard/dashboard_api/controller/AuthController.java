package com.dev.dashboard.dashboard_api.controller;

import com.dev.dashboard.dashboard_api.dto.LoginResponse;
import com.dev.dashboard.dashboard_api.dto.LoginRequest;
import com.dev.dashboard.dashboard_api.dto.RegisterRequest;
import com.dev.dashboard.dashboard_api.dto.RegisterResponse;
import com.dev.dashboard.dashboard_api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register (@Valid @RequestBody RegisterRequest registerRequest){
        RegisterResponse response =authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login (@Valid @RequestBody LoginRequest loginRequest){
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
