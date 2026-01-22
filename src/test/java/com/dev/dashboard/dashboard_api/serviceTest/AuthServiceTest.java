package com.dev.dashboard.dashboard_api.serviceTest;

import com.dev.dashboard.dashboard_api.dto.RegisterRequest;
import com.dev.dashboard.dashboard_api.dto.RegisterResponse;
import com.dev.dashboard.dashboard_api.entity.Account;
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
public class AuthServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

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

        // Mantık: Sonra ne yapıyor? Şifreyi encode ediyor.
        // Encoder taklidine diyorum ki: "Sana ne verirlerse versinler, 'sifreli_metin' diye bir string dön."
        when(passwordEncoder.encode(any())).thenReturn("sifreli_metin");

        // --- 2. ACT (Motor!) ---
        // Şimdi asıl oğlanı sahneye itiyoruz. "Hadi bakalım, kayıt yap."
        RegisterResponse response = authService.register(request);

        // --- 3. ASSERT (Montaj ve Kontrol) ---
        // Bakalım sonuç istediğimiz gibi mi?

        // Kontrol 1: Mesaj doğru mu?
        assertEquals("Kayıt başarıyla tamamlandı.", response.message());

        // Kontrol 2 (En önemlisi): Aşçı gerçekten yemeği fırına verdi mi?
        // Yani; Repository'nin 'save' metodunu çağırdı mı? Çağırmadıysa kayıt olmamıştır çünkü.
        verify(accountRepository, times(1)).save(any(Account.class));
    }
    @Test
    void register_ShouldFail_WhenEmailExists() {
        // --- ARRANGE ---
        // Repository taklidine yeni emir: "Bu sefer sana sorulursa 'TRUE' (var) de."
        when(accountRepository.existsByEmail(request.email())).thenReturn(true);

        // --- ACT & ASSERT ---
        // Mantık: Burada AuthService.register'ı çağırdığım an kodun patlaması (Exception atması) lazım.
        // Eğer patlamazsa test başarısız sayılır!
        assertThrows(RuntimeException.class, () -> {
            authService.register(request);
        });

        // Mantık: Hata verdi, güzel. Peki hata verdi ama "yanlışlıkla" kaydetmeye çalıştı mı?
        // Bunu kontrol etmezsek, hatalı kullanıcıyı veritabanına yazabilir!
        verify(accountRepository, never()).save(any()); // ASLA kaydetme çağırmamalı.
    }
}
