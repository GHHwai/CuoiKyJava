package com.chatapp.chat_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.chatapp.chat_backend.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                // ===== PUBLIC API =====
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/ws/**").permitAll()

                // ===== STATIC FILES =====
                .requestMatchers(
                        "/",
                        "/index.html",
                        "/login.html",
                        "/register.html",
                        "/chat.html",
                        "/auth.js",
                        "/chat.js",
                        "/style.css",
                        "/favicon.ico"
                ).permitAll()

                // ===== API CẦN JWT =====
                .requestMatchers("/api/users/**").authenticated()
                .requestMatchers("/api/friends/**").authenticated()
                .requestMatchers("/api/conversations/**").authenticated()
                .requestMatchers("/api/messages/**").authenticated()

                // ===== ERROR =====
                .requestMatchers("/error").permitAll()

                // ===== CÒN LẠI =====
                .anyRequest().authenticated()
            )

            .addFilterBefore(
                    jwtFilter,
                    UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}