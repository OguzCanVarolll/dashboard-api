package com.dev.dashboard.dashboard_api.serviceTest;

import com.dev.dashboard.dashboard_api.dto.LoginRequest;
import com.dev.dashboard.dashboard_api.dto.LoginResponse;
import com.dev.dashboard.dashboard_api.dto.RegisterRequest;
import com.dev.dashboard.dashboard_api.dto.RegisterResponse;
import com.dev.dashboard.dashboard_api.entity.Account;
import com.dev.dashboard.dashboard_api.entity.Role;
import com.dev.dashboard.dashboard_api.exception.EmailAlreadyExistsException;
import com.dev.dashboard.dashboard_api.repository.AccountRepository;
import com.dev.dashboard.dashboard_api.service.AuthService;
import com.dev.dashboard.dashboard_api.service.JwtService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {
    @Mock
    private Account account;

    @Mock
    private Authentication authentication;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private LoginRequest request;

    @BeforeEach
    void setUp() {
        request = new LoginRequest("user@gmail.com","password");
    }
    @AfterEach
    void tearDown(){
        SecurityContextHolder.clearContext();
    }
    @Test
    void login_ShouldSuccess_WhenCredentialsValid() {
        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);
        when(authentication.getPrincipal())
                .thenReturn(account);
        when(account.getAccountUsername())
                .thenReturn("User");
        when(jwtService.generateToken(account))
                .thenReturn("access-token");
        when(jwtService.generateRefreshToken(account))
                .thenReturn("refresh-token");
        LoginResponse response = authService.login(request);
        assertEquals("Giriş Başarılı", response.message());
        assertEquals("access-token", response.accessToken());
        assertEquals("refresh-token", response.refreshToken());
        assertEquals("User",response.username());
        verify(authenticationManager,times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
    @Test
    void login_ShouldFail_WhenCredentialsNotValid() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));
        assertThrows(BadCredentialsException.class, () ->
            authService.login(request));

        verify(jwtService, never()).generateToken(any());
    }
}
