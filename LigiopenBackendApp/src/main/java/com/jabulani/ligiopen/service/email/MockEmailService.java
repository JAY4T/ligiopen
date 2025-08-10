package com.jabulani.ligiopen.service.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MockEmailService implements EmailService {

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;
    
    @Value("${app.frontend-url:http://localhost:3000}")
    private String frontendUrl;

    @Override
    public void sendVerificationEmail(String to, String username, String verificationToken) {
        log.info("📧 MOCK EMAIL SERVICE - Verification email");
        log.info("📧 To: {}", to);
        log.info("📧 Username: {}", username);
        log.info("📧 Subject: Verify Your LigiOpen Account");
        log.info("📧 Verification URL: {}/api/auth/v1/verify-email?token={}", baseUrl, verificationToken);
        log.info("📧 ========================================");
    }

    @Override
    public void sendPasswordResetEmail(String to, String username, String resetToken) {
        log.info("🔐 MOCK EMAIL SERVICE - Password Reset email");
        log.info("🔐 To: {}", to);
        log.info("🔐 Username: {}", username);
        log.info("🔐 Subject: Reset Your LigiOpen Password");
        log.info("🔐 Reset URL: {}/reset-password?token={}", frontendUrl, resetToken);
        log.info("🔐 ========================================");
    }

    @Override
    public void sendWelcomeEmail(String to, String username) {
        log.info("🎉 MOCK EMAIL SERVICE - Welcome email");
        log.info("🎉 To: {}", to);
        log.info("🎉 Username: {}", username);
        log.info("🎉 Subject: Welcome to LigiOpen!");
        log.info("🎉 Welcome message would be sent to new user");
        log.info("🎉 ========================================");
    }
}