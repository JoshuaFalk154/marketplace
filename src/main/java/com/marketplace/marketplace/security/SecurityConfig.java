package com.marketplace.marketplace.security;

import com.marketplace.marketplace.jwt.JwtAuthConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthConverter converter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/hello").permitAll();
            authorize.anyRequest().authenticated();
        })
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(jwt ->
                        jwt.jwtAuthenticationConverter(converter)

                ))
                .build();
    }
}
