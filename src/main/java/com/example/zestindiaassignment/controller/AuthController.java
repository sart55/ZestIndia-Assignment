package com.example.zestindiaassignment.controller;

import com.example.zestindiaassignment.dto.ApiResponse;
import com.example.zestindiaassignment.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "APIs for authentication and token management")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Generate JWT token for user")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(@RequestParam String username) {
        log.info("Login request for user: {}", username);
        String token = jwtTokenProvider.generateToken(username);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("tokenType", "Bearer");
        response.put("username", username);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @PostMapping("/validate")
    @Operation(summary = "Validate JWT token", description = "Validate if a JWT token is valid")
    public ResponseEntity<ApiResponse<Map<String, Object>>> validateToken(@RequestParam String token) {
        log.info("Token validation request");
        boolean isValid = jwtTokenProvider.validateToken(token);
        Map<String, Object> response = new HashMap<>();
        response.put("valid", isValid);

        if (isValid) {
            String username = jwtTokenProvider.getUsernameFromToken(token);
            response.put("username", username);
        }

        return ResponseEntity.ok(ApiResponse.success("Validation result", response));
    }
}

