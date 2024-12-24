package com.example.captiveportal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.captiveportal.components.IpAddressUtil;
import com.example.captiveportal.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PortalController {
    private final AuthService authService;
    private final IpAddressUtil ipAddressUtil;

    @GetMapping("/")
    public String index(HttpServletRequest request) {
        String ipAddress = ipAddressUtil.getClientIp(request);
        if (!authService.isAuthenticated(ipAddress)) {
            return "redirect:/login";
        }
        return "redirect:" + request.getHeader("X-Original-URL");
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";  // akan render login.html
    }
    @GetMapping("/register")
    public String registerPage() {
        return "register";  // akan render register.html
    }
}