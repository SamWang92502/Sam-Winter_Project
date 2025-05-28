package com.sam.urlshortener.service;

import com.sam.urlshortener.util.JwtUtils;
import com.sam.urlshortener.dto.CreateUserRequest;
import com.sam.urlshortener.model.User;
import com.sam.urlshortener.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import java.util.Collections;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public User createUser(CreateUserRequest request) {
        String username = request.getUsername();
        String email = request.getEmail();
        String password = request.getPassword();

        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already registered");
        }

        String encryptedPassword = passwordEncoder.encode(password);
        User user = new User(username, email, encryptedPassword);
        return userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public ResponseEntity<?> authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        // Generate JWT
        String token = jwtUtils.generateJwtToken(user.getUsername());

        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

    public String generateTokenForUser(String username) {
        return jwtUtils.generateJwtToken(username);
    }

}
