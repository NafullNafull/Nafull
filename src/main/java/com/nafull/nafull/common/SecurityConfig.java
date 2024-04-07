package com.nafull.nafull.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(it -> it.configurationSource(
                        _unused -> {
                            CorsConfiguration configuration = new CorsConfiguration();
                            configuration.addAllowedOrigin("http://localhost:3000");
                            configuration.addAllowedOrigin("http://10.10.6.114:3000");
                            configuration.addAllowedOrigin("http://10.10.6.126:3000");
                            configuration.addAllowedOrigin("https://204e-183-96-52-165.ngrok-free.app");
                            configuration.addAllowedOrigin("https://nafull-client.vercel.app");
                            configuration.setAllowedMethods(List.of("*"));
                            configuration.setAllowedHeaders(List.of("*"));
                            configuration.setAllowCredentials(true);
                            return configuration;
                        }
                ))
                .authorizeHttpRequests(it -> it.anyRequest().permitAll())
                .sessionManagement(it -> it.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}
