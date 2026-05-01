package com.example.traveljournal.controller;

import com.example.traveljournal.dto.AuthResponse;
import com.example.traveljournal.dto.LoginRequest;
import com.example.traveljournal.dto.RegisterRequest;
import com.example.traveljournal.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * POST /api/auth/register
     * Accepts: username, email, password
     * Returns: 201 Created with JWT token
     * Returns: 409 Conflict if email already registered
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * POST /api/auth/login
     * Accepts: email, password
     * Returns: 200 OK with JWT token
     * Returns: 401 Unauthorized for invalid credentials (generic message only)
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
