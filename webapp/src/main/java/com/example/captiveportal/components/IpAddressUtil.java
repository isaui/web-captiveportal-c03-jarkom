package com.example.captiveportal.components;

import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class IpAddressUtil {
    public String getClientIp(HttpServletRequest request) {
        // Coba ambil dari X-Real-IP yang di-set Nginx
        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isEmpty()) {
            return realIp;
        }

        // Fallback ke remote address jika header tidak ada
        return request.getRemoteAddr();
    }
}