package com.gestionHospitalaria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers
                .frameOptions(frame -> frame.disable())
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/login",
                    "/registro",
                    "/citas",
                    "/citas/crear",
                    "/citas/ver",
                    "/h2-console/**",
                    "/favicon.ico", 
                    "/css/**",
                    "/api/pacientes/**",
                    "/api/citas/**"
                ).permitAll()
                .anyRequest().permitAll()
            )
            .formLogin(form -> form.disable());

        return http.build();
    }
}