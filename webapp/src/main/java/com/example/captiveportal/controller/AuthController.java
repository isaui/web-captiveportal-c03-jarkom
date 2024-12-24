package com.example.captiveportal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.captiveportal.components.IpAddressUtil;
import com.example.captiveportal.models.UserSession;
import com.example.captiveportal.models.dto.LoginRequest;
import com.example.captiveportal.models.dto.LoginResponse;
import com.example.captiveportal.models.dto.MessageResponse;
import com.example.captiveportal.models.dto.RegisterRequest;
import com.example.captiveportal.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final IpAddressUtil ipAddressUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            authService.register(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(new MessageResponse("User registered successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        try {
            String ipAddress = ipAddressUtil.getClientIp(httpRequest);
            UserSession session = authService.login(request.getUsername(), request.getPassword(), ipAddress);
            return ResponseEntity.ok()
                .body(new LoginResponse("Login successful", session.getSessionToken()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkAuth(HttpServletRequest request) {
        String ipAddress = ipAddressUtil.getClientIp(request);
        boolean isAuthenticated = authService.isAuthenticated(ipAddress);
        
        if (!isAuthenticated) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new MessageResponse("Authentication required"));
        }
        return ResponseEntity.ok(new MessageResponse("Authenticated"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String ipAddress = ipAddressUtil.getClientIp(request);
        authService.logout(ipAddress);
        return ResponseEntity.ok(new MessageResponse("Logged out successfully"));
    }
}

