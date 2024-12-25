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
            authService.login(request.getUsername(), request.getPassword(), ipAddress);
            return "redirect:/success";
        } catch (RuntimeException e) {
            System.out.println(e);
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
        if (originalUrl == null || originalUrl.isEmpty()) {
            return "success";  // akan merender success.html
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
    public String successPage(Model model) {
        return "success";
    }
}