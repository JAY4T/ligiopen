package com.jabulani.ligiopen.service.email;

public interface EmailService {
    
    /**
     * Send email verification email to user
     * @param to recipient email address
     * @param username user's name
     * @param verificationToken verification token
     */
    void sendVerificationEmail(String to, String username, String verificationToken);
    
    /**
     * Send password reset email to user
     * @param to recipient email address
     * @param username user's name
     * @param resetToken password reset token
     */
    void sendPasswordResetEmail(String to, String username, String resetToken);
    
    /**
     * Send welcome email after successful verification
     * @param to recipient email address
     * @param username user's name
     */
    void sendWelcomeEmail(String to, String username);
}