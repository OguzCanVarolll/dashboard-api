package com.dev.dashboard.dashboard_api.service;

import com.dev.dashboard.dashboard_api.dto.LoginRequest;
import com.dev.dashboard.dashboard_api.dto.LoginResponse;
import com.dev.dashboard.dashboard_api.dto.RegisterRequest;
import com.dev.dashboard.dashboard_api.entity.Account;
import com.dev.dashboard.dashboard_api.entity.Role;
import com.dev.dashboard.dashboard_api.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void register (RegisterRequest request){

        if (!request.password().equals(request.confirmPassword())){
            throw new RuntimeException("Passwords do not match");
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        Account account = Account.builder()
                .email(request.email())
                .hashPassword(encodedPassword)
                .role(Role.USER)
                .build();

        accountRepository.save(account);
    }

    public LoginResponse login(LoginRequest request){
       Account account = accountRepository.findByEmail(request.email())
               .orElseThrow(()-> new RuntimeException("Kullanıcı bulunamadı."));

       if(!passwordEncoder.matches(request.password(), account.getHashPassword())){
           throw new RuntimeException("Hatalı şifre veya mail");
       }

       String token = jwtService.generateToken(account.getEmail());

       return new LoginResponse(token);
    }

}
