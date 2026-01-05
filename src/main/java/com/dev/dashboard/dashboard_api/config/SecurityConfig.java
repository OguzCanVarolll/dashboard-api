package com.dev.dashboard.dashboard_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

   @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
       http
               .csrf(AbstractHttpConfigurer::disable)
               .cors(cors -> cors.configurationSource(request-> {
                   var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                   corsConfiguration.setAllowedOrigins(java.util.List.of("http://localhost:5173"));
                   corsConfiguration.setAllowedMethods(java.util.List.of("GET","POST","PUT","DELETE","OPTIONS"));
                   corsConfiguration.setAllowedHeaders(java.util.List.of("*"));
                   corsConfiguration.setAllowCredentials(true);
                   return corsConfiguration;
               }))
               .formLogin(AbstractHttpConfigurer::disable)
               .httpBasic(AbstractHttpConfigurer::disable)
               .authorizeHttpRequests(auth ->auth
                       .requestMatchers(
                           "/api/v1/auth/**",
                           "/swagger-ui/**",
                           "/v3/api-docs/**",
                           "/v3/api-docs.yaml",
                           "/swagger-resources/**",
                           "/webjars/**"
                       ).permitAll()
                       .anyRequest().authenticated()
               )
               .sessionManagement(session ->
                       session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
               );

       return http.build();
   }
}
