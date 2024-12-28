package user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        // Or define a @Bean in a @Configuration class for PasswordEncoder
    }

    public User createUser(String username, String email, String password) {
        // 1. Validate input (e.g., check if user exists)
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already in use");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }

        // 2. Hash password
        String hashedPassword = passwordEncoder.encode(password);

        // 3. Create and save user
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(hashedPassword);

        return userRepository.save(user);
    }

    // Optionally: find user by username, authenticate, etc.
}
