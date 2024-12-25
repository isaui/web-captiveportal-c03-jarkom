package com.example.captiveportal.components;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class IpAddressUtil {
    public String getClientIp(HttpServletRequest request) {
        // Prioritaskan X-Real-IP header
        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isEmpty()) {
            System.out.println("Using X-Real-IP: " + realIp);
            return realIp;
        }

        // Fallback ke X-Forwarded-For
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isEmpty()) {
            System.out.println("Using X-Forwarded-For: " + forwardedFor);
            return forwardedFor.split(",")[0].trim();
        }

        // Terakhir gunakan remote address
        String remoteAddr = request.getRemoteAddr();
        System.out.println("Using RemoteAddr: " + remoteAddr);
        return remoteAddr;
    }
}