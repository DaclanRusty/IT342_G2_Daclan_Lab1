package com.daclan.userauth.Controller;

import com.daclan.userauth.Service.AuthService;
import com.daclan.userauth.dto.AuthResponse;
import com.daclan.userauth.dto.LoginRequest;
import com.daclan.userauth.dto.RegisterRequest;
import com.daclan.userauth.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Registration successful");

            Map<String, String> user = new HashMap<>();
            user.put("email", response.getEmail());
            user.put("firstName", response.getFirstName());
            user.put("lastName", response.getLastName());

            responseBody.put("user", user);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("token", response.getToken());

            Map<String, String> user = new HashMap<>();
            user.put("email", response.getEmail());
            user.put("firstName", response.getFirstName());
            user.put("lastName", response.getLastName());

            responseBody.put("user", user);

            return ResponseEntity.ok(responseBody);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String token) {
        try {
            String email = extractEmailFromToken(token);
            UserResponse user = authService.getCurrentUser(email);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    private String extractEmailFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return "email@example.com";
    }
}