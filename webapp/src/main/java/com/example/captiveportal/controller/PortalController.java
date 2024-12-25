package com.example.captiveportal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.captiveportal.components.IpAddressUtil;
import com.example.captiveportal.models.dto.LoginRequest;
import com.example.captiveportal.models.dto.RegisterRequest;
import com.example.captiveportal.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
@Controller
@RequiredArgsConstructor
public class PortalController {
    private final AuthService authService;
    private final IpAddressUtil ipAddressUtil;


    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest request,
                    HttpServletRequest httpRequest,
                    RedirectAttributes redirectAttributes) {
    try {
        String ipAddress = ipAddressUtil.getClientIp(httpRequest);
        System.out.println("Login attempt from IP: " + ipAddress);
        authService.login(request.getUsername(), request.getPassword(), ipAddress);
        System.out.println("Login successful");
        return "redirect:/success";
    } catch (RuntimeException e) {
        System.out.println("Login failed: " + e.getMessage());
        redirectAttributes.addAttribute("error", e.getMessage());
        return "redirect:/login";
    }
}

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterRequest request, 
                         RedirectAttributes redirectAttributes) {
        try {
            authService.register(request.getUsername(), request.getPassword());
            return "redirect:/login?success=true";
        } catch (RuntimeException e) {
            System.out.println(e);
            redirectAttributes.addAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/")
    public String index(HttpServletRequest request) {
        String ipAddress = ipAddressUtil.getClientIp(request);
        if (!authService.isAuthenticated(ipAddress)) {
            return "redirect:/login";
        }
        
        String originalUrl = request.getHeader("X-Original-URL");
        System.out.println("Original URL: " + originalUrl); // Debug
        
        if (originalUrl == null || originalUrl.isEmpty() || originalUrl.equals("/")) {
            return "success";
        }
        
        return "redirect:" + originalUrl;
    }
    @GetMapping("/login")
    public String loginPage(Model model, @RequestParam(required = false) String error) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model, @RequestParam(required = false) String error) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "register";
    }

    @GetMapping("/success")
    public String successPage(HttpServletRequest request) {
    String ipAddress = ipAddressUtil.getClientIp(request);
    System.out.println("Success page accessed by IP: " + ipAddress);
    if (!authService.isAuthenticated(ipAddress)) {
        return "redirect:/login";
    }
    return "success";
}
}