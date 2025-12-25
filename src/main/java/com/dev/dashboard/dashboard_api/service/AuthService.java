package com.dev.dashboard.dashboard_api.service;

import com.dev.dashboard.dashboard_api.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public void register (RegisterRequest request){

        if (!request.password().equals(request.confirmPassword())){
            throw new RuntimeException("Passwords do not match");
        }
        
    }

}
