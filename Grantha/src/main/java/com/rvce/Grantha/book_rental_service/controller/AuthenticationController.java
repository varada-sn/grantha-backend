package com.rvce.Grantha.book_rental_service.controller;

import com.rvce.Grantha.book_rental_service.dto.LoginDTO;
import com.rvce.Grantha.book_rental_service.dto.UserRegistrationDTO;
import com.rvce.Grantha.book_rental_service.security.JwtUtil;
import com.rvce.Grantha.book_rental_service.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authService;

    @Autowired
    private JwtUtil jwtUtil; // 🔹 Injecting JWT Utility

    //Login API - Now Returns JWT*
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginDTO loginDto) {
        String authenticatedRole = authService.authenticateUser(
                loginDto.getEmail(),
                loginDto.getPassword(),
                loginDto.getRole()
        );

        Map<String, String> response = new HashMap<>();

        if (!authenticatedRole.equals("INVALID")) {
            String token = jwtUtil.generateToken(loginDto.getEmail(), authenticatedRole); // 🔹 Generate Token
            response.put("token", token);
            response.put("role", authenticatedRole);
        } else {
            response.put("error", "Invalid credentials");
        }

        return response;
    }

    //Signup API (Handles both Customer & Supplier)*
    @PostMapping("/signup")
    public Map<String, String> registerUser(@RequestBody UserRegistrationDTO userDto) {
        String resp = authService.registerUser(userDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", resp);
        return response;
    }
}