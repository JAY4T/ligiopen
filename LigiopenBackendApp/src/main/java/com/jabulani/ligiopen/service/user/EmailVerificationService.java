package com.jabulani.ligiopen.service.user;

import com.jabulani.ligiopen.dao.EmailVerificationTokenDao;
import com.jabulani.ligiopen.dao.UserEntityDao;
import com.jabulani.ligiopen.entity.user.EmailVerificationToken;
import com.jabulani.ligiopen.entity.user.UserEntity;
import com.jabulani.ligiopen.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailVerificationService {

    private final EmailVerificationTokenDao tokenDao;
    private final UserEntityDao userDao;
    private final EmailService emailService;

    /**
     * Generate and send email verification token to user
     */
    @Async
    @Transactional
    public void sendVerificationEmail(UserEntity user) {
        try {
            // Delete any existing unused tokens for this user
            tokenDao.deleteByUser(user);
            
            // Generate new verification token
            String token = generateVerificationToken();
            
            // Save token to database
            EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                    .token(token)
                    .user(user)
                    .build();
            
            tokenDao.save(verificationToken);
            
            // Send email
            String username = user.getFirstName() != null ? user.getFirstName() : user.getUsername();
            emailService.sendVerificationEmail(user.getEmail(), username, token);
            
            log.info("Verification email sent to user: {}", user.getEmail());
            
        } catch (Exception e) {
            log.error("Failed to send verification email to user: {}", user.getEmail(), e);
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    /**
     * Verify email using token
     */
    @Transactional
    public boolean verifyEmail(String token) {
        try {
            Optional<EmailVerificationToken> tokenOptional = tokenDao.findByTokenAndUsedFalse(token);
            
            if (tokenOptional.isEmpty()) {
                log.warn("Invalid verification token: {}", token);
                return false;
            }
            
            EmailVerificationToken verificationToken = tokenOptional.get();
            
            if (verificationToken.isExpired()) {
                log.warn("Expired verification token for user: {}", verificationToken.getUser().getEmail());
                return false;
            }
            
            // Mark token as used
            verificationToken.setUsed(true);
            tokenDao.save(verificationToken);
            
            // Mark user as verified
            UserEntity user = verificationToken.getUser();
            user.setEmailVerified(true);
            userDao.updateUser(user);
            
            // Send welcome email (async, non-critical)
            sendWelcomeEmailAsync(user);
            
            log.info("Email verified successfully for user: {}", user.getEmail());
            return true;
            
        } catch (Exception e) {
            log.error("Failed to verify email with token: {}", token, e);
            return false;
        }
    }

    /**
     * Resend verification email to user
     */
    @Transactional
    public boolean resendVerificationEmail(String email) {
        try {
            Optional<UserEntity> userOptional = userDao.getUserByEmail(email);
            
            if (userOptional.isEmpty()) {
                log.warn("Attempted to resend verification to non-existent user: {}", email);
                return false;
            }
            
            UserEntity user = userOptional.get();
            
            if (user.isEmailVerified()) {
                log.info("User already verified, no need to resend: {}", email);
                return false;
            }
            
            sendVerificationEmail(user);
            return true;
            
        } catch (Exception e) {
            log.error("Failed to resend verification email to: {}", email, e);
            return false;
        }
    }

    /**
     * Check if user has pending verification
     */
    public boolean hasPendingVerification(UserEntity user) {
        Optional<EmailVerificationToken> token = tokenDao.findByUserAndUsedFalse(user);
        return token.isPresent() && !token.get().isExpired();
    }

    /**
     * Send welcome email asynchronously
     */
    @Async
    private void sendWelcomeEmailAsync(UserEntity user) {
        try {
            String username = user.getFirstName() != null ? user.getFirstName() : user.getUsername();
            emailService.sendWelcomeEmail(user.getEmail(), username);
        } catch (Exception e) {
            log.warn("Failed to send welcome email to: {}", user.getEmail(), e);
            // Don't throw exception - welcome email is not critical
        }
    }

    /**
     * Clean up expired tokens (runs daily at 2 AM)
     */
    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void cleanupExpiredTokens() {
        try {
            List<EmailVerificationToken> expiredTokens = tokenDao.findByExpiryDateBeforeAndUsedFalse(LocalDateTime.now());
            
            if (!expiredTokens.isEmpty()) {
                tokenDao.deleteAll(expiredTokens);
                log.info("Cleaned up {} expired verification tokens", expiredTokens.size());
            }
            
        } catch (Exception e) {
            log.error("Failed to cleanup expired tokens", e);
        }
    }

    /**
     * Generate secure verification token
     */
    private String generateVerificationToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}