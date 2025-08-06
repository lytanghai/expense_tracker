package com.tanghai.expense_tracker.controller;

import com.tanghai.expense_tracker.dto.req.AuthRequest;
import com.tanghai.expense_tracker.service.AuthService;
import com.tanghai.expense_tracker.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/public/auth")
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Value("${app.version}")
    private String appVersion;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        if (authService.isValid(request.getUsername(), request.getPassword())) {
            String token = authService.generateToken(request.getUsername());
            Map<String,Object> map = new HashMap<>();
            map.put("token", token);
            map.put("username", JwtUtil.extractUsername(token, authService.getSecretKey()));
            map.put("app_version", appVersion);
            return ResponseEntity.ok().body(map);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @GetMapping("/secure")
    public ResponseEntity<?> secure(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        if (JwtUtil.isTokenValid(token, authService.getSecretKey())) {
            return ResponseEntity.ok().body("Secure access granted to " + JwtUtil.extractUsername(token, authService.getSecretKey()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
    }
}