package com.daclan.userauth.Service;

import com.daclan.userauth.Entity.User;
import com.daclan.userauth.Repository.UserRepository;
import com.daclan.userauth.Util.JwtUtil;
import com.daclan.userauth.dto.AuthResponse;
import com.daclan.userauth.dto.LoginRequest;
import com.daclan.userauth.dto.RegisterRequest;
import com.daclan.userauth.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        // BACKEND VALIDATIONS

        // Check if fields are null or empty
        if (request.getFirstName() == null || request.getFirstName().trim().isEmpty()) {
            throw new RuntimeException("First name is required");
        }

        if (request.getLastName() == null || request.getLastName().trim().isEmpty()) {
            throw new RuntimeException("Last name is required");
        }

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email is required");
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new RuntimeException("Password is required");
        }

        // Validate name lengths
        if (request.getFirstName().trim().length() < 2) {
            throw new RuntimeException("First name must be at least 2 characters");
        }

        if (request.getLastName().trim().length() < 2) {
            throw new RuntimeException("Last name must be at least 2 characters");
        }

        // Validate names contain only letters and spaces
        if (!request.getFirstName().matches("^[a-zA-Z\\s]+$")) {
            throw new RuntimeException("First name should only contain letters");
        }

        if (!request.getLastName().matches("^[a-zA-Z\\s]+$")) {
            throw new RuntimeException("Last name should only contain letters");
        }

        // Validate email format
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!request.getEmail().matches(emailRegex)) {
            throw new RuntimeException("Invalid email format");
        }

        // Validate password strength
        if (request.getPassword().length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters");
        }

        if (!request.getPassword().matches(".*[a-z].*")) {
            throw new RuntimeException("Password must contain at least one lowercase letter");
        }

        if (!request.getPassword().matches(".*[A-Z].*")) {
            throw new RuntimeException("Password must contain at least one uppercase letter");
        }

        if (!request.getPassword().matches(".*\\d.*")) {
            throw new RuntimeException("Password must contain at least one number");
        }

        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail().toLowerCase().trim())) {
            throw new RuntimeException("Email already registered");
        }

        // Create new user
        User user = new User();
        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());
        user.setEmail(request.getEmail().toLowerCase().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        // Generate token
        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(token, user.getEmail(), user.getFirstName(), user.getLastName());
    }

    public AuthResponse login(LoginRequest request) {
        // BACKEND VALIDATIONS

        // Check if fields are null or empty
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email is required");
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new RuntimeException("Password is required");
        }

        // Validate email format
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!request.getEmail().matches(emailRegex)) {
            throw new RuntimeException("Invalid email format");
        }

        // Find user by email
        User user = userRepository.findByEmail(request.getEmail().toLowerCase().trim())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // Generate token
        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(token, user.getEmail(), user.getFirstName(), user.getLastName());
    }

    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponse(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}