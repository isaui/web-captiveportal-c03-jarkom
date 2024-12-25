package com.example.captiveportal.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.captiveportal.models.User;
import com.example.captiveportal.models.UserSession;
import com.example.captiveportal.repository.UserRepository;
import com.example.captiveportal.repository.UserSessionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserSessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    @Transactional
    public UserSession login(String username, String password, String ipAddress) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Clear any existing session for this IP
        sessionRepository.deleteByIpAddress(ipAddress);

        // Create new session
        UserSession session = new UserSession();
        session.setUser(user);
        session.setIpAddress(ipAddress);
        session.setCreatedAt(LocalDateTime.now());
        session.setSessionToken(generateSessionToken());
        
        return sessionRepository.save(session);
    }

    @Transactional
    public boolean validateSession(String sessionToken, String ipAddress) {
        Optional<UserSession> sessionOpt = sessionRepository.findBySessionToken(sessionToken);
        
        if (sessionOpt.isEmpty()) {
            return false;
        }

        UserSession session = sessionOpt.get();
        
        // Validate IP matches
        if (!session.getIpAddress().equals(ipAddress)) {
            return false;
        }

        // Check if session is expired (24 hours)
        if (LocalDateTime.now().isAfter(session.getCreatedAt().plusHours(24))) {
            sessionRepository.delete(session);
            return false;
        }

        return true;
    }

    @Transactional
    public void logout(String ipAddress) {
        sessionRepository.deleteByIpAddress(ipAddress);
    }

    private String generateSessionToken() {
        return UUID.randomUUID().toString();
    }

    public boolean isAuthenticated(String ipAddress) {
        Optional<UserSession> session = sessionRepository.findByIpAddress(ipAddress);
        return session.isPresent() && 
               !LocalDateTime.now().isAfter(session.get().getCreatedAt().plusMinutes(5));
    }
}