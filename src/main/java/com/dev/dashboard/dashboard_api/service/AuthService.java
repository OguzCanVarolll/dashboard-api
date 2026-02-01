package com.dev.dashboard.dashboard_api.service;

import com.dev.dashboard.dashboard_api.dto.LoginRequest;
import com.dev.dashboard.dashboard_api.dto.RegisterRequest;
import com.dev.dashboard.dashboard_api.dto.LoginResponse;
import com.dev.dashboard.dashboard_api.dto.RegisterResponse;
import com.dev.dashboard.dashboard_api.entity.Account;
import com.dev.dashboard.dashboard_api.entity.Role;
import com.dev.dashboard.dashboard_api.exception.EmailAlreadyExistsException;
import com.dev.dashboard.dashboard_api.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public RegisterResponse register (RegisterRequest request){
        log.info("Yeni kullanıcı kayıt isteği başladı");

        if(accountRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }
        Account account = Account.builder()
                .accountUsername(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        accountRepository.save(account);
        log.info("Kullanıcı başarıyla kaydedildi");

        return new RegisterResponse("Kayıt başarıyla tamamlandı.");
    }

    public LoginResponse login(LoginRequest request){
        log.info("Giriş denemesi yapılıyor: {}", request.email());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        Account account = (Account) authentication.getPrincipal();

        log.info("Giriş başarılı , Token üretiliyor...");

       return new LoginResponse(
               "Giriş Başarılı",
               jwtService.generateToken(account),
               jwtService.generateRefreshToken(account),
               account.getAccountUsername()
       );
    }


}
