package com.example.captiveportal.controller;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.captiveportal.components.IpAddressUtil;
import com.example.captiveportal.models.dto.MessageResponse;
import com.example.captiveportal.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthApiController {
    
    private final AuthService authService;
    private final IpAddressUtil ipAddressUtil;

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
