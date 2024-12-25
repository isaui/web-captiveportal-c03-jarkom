package com.example.captiveportal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.captiveportal.models.UserSession;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findBySessionToken(String token);
    Optional<UserSession> findByIpAddress(String ipAddress);
    void deleteByIpAddress(String ipAddress);
}