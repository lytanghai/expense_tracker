package com.tanghai.expense_tracker.service;

import com.tanghai.expense_tracker.util.JwtUtil;
import com.tanghai.expense_tracker.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final String secretKey;
    private final String adminUser;
    private final String adminPwd;

    public AuthService(
            @Value("${jwt.secret_key}") String secretKey,
            @Value("${jwt.auth.users.admin_usr}") String adminUser,
            @Value("${jwt.auth.users.admin_pwd}") String adminPwd // use raw passwords in env!
    ) {
        this.secretKey = secretKey;
        this.adminUser = adminUser;
        this.adminPwd = adminPwd;
    }

    public boolean isValid(String username, String password) {
        return adminUser.equals(username) && PasswordUtil.matches(adminPwd, password);
    }

    public String generateToken(String username) {
        return JwtUtil.generateToken(username, secretKey);
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getAdminUser() {
        return adminUser;
    }

    public String getAdminPwd() {
        return adminPwd;
    }
}