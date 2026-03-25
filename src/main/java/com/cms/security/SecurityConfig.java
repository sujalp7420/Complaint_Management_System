package com.cms.security;

import com.cms.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()

                        // Category endpoints
                        .requestMatchers(HttpMethod.GET, "/api/categories").hasAnyRole("USER", "ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").hasAnyRole("USER", "ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.POST, "/api/categories").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("ADMIN")

                        // Complaint endpoints
                        .requestMatchers(HttpMethod.POST, "/api/complaints").hasAnyRole("USER", "ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/complaints/user/{userId}").hasAnyRole("USER", "ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/complaints/assigned/{userId}").hasAnyRole("USER", "ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.PUT, "/api/complaints/{id}").hasAnyRole("USER", "ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/complaints/{id}").hasAnyRole("USER", "ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/complaints").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/complaints/status/{status}").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.PATCH, "/api/complaints/{id}/status").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.DELETE, "/api/complaints/{id}").hasAnyRole("USER", "ADMIN", "STAFF")

                        // Comment and Attachment endpoints
                        .requestMatchers("/api/comments/**").hasAnyRole("USER", "ADMIN", "STAFF")
                        .requestMatchers("/api/attachments/**").hasAnyRole("USER", "ADMIN", "STAFF")

                        // Admin only endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/role/{role}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/users/{id}/status").hasRole("ADMIN")

                        // User management endpoints
                        .requestMatchers(HttpMethod.GET, "/api/users").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/users/{id}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/users/email/{email}").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/users/{id}").authenticated()

                        // Any other request requires authentication
                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> {})
                .userDetailsService(userDetailsService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}