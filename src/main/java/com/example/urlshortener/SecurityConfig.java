package com.example.urlshortener;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF protection for APIs. Consider enabling it if your application requires it.
                .csrf(csrf -> csrf.disable())

                // Configure authorization rules
                .authorizeHttpRequests(authz -> authz
                        // Allow anyone to perform POST requests to /users (registration)
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()

                        // Allow access to H2 console if you're using it (optional)
                        // .requestMatchers("/h2-console/**").permitAll()

                        // Require authentication for any other requests
                        .anyRequest().authenticated()
                )

                // Enable HTTP Basic Authentication
                .httpBasic(Customizer.withDefaults());

        // If you're using H2 database and want to access the H2 console, you might need to disable frame options
        // http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}
