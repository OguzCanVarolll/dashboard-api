package com.dev.dashboard.dashboard_api.serviceTest;

import com.dev.dashboard.dashboard_api.dto.RegisterRequest;
import com.dev.dashboard.dashboard_api.dto.RegisterResponse;
import com.dev.dashboard.dashboard_api.entity.Account;
import com.dev.dashboard.dashboard_api.exception.EmailAlreadyExistsException;
import com.dev.dashboard.dashboard_api.repository.AccountRepository;
import com.dev.dashboard.dashboard_api.service.AuthService;
import com.dev.dashboard.dashboard_api.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegisterServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private AuthService authService;

    private RegisterRequest request;

    @BeforeEach
    void setUp() {
        request = new RegisterRequest("User","user@gmail.com","password");
    }
    @Test
    void register_ShouldSuccess_WhenEmailIsUnique() {
        when(accountRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("sifreli_metin");
        RegisterResponse response = authService.register(request);
        assertEquals("Kayıt başarıyla tamamlandı.", response.message());
        verify(accountRepository, times(1)).save(any(Account.class));
    }
    @Test
    void register_ShouldFail_WhenEmailExists() {
        when(accountRepository.existsByEmail(request.email())).thenReturn(true);
        assertThrows(EmailAlreadyExistsException.class, () -> {
            authService.register(request);
        });
        verify(accountRepository, never()).save(any());
    }
}
