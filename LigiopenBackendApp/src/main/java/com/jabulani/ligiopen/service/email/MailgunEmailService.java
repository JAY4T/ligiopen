package com.jabulani.ligiopen.service.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sargue.mailgun.Configuration;
import net.sargue.mailgun.Mail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// @Service - Commented out until Mailgun is configured
@RequiredArgsConstructor
@Slf4j
public class MailgunEmailService implements EmailService {

    // private final Configuration mailgunConfiguration; - Commented out
    
    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;
    
    @Value("${app.frontend-url:http://localhost:3000}")
    private String frontendUrl;

    @Override
    public void sendVerificationEmail(String to, String username, String verificationToken) {
        // TODO: Implement when Mailgun is configured
        log.info("MOCK EMAIL - Verification email would be sent to: {} with token: {}", to, verificationToken);
        log.info("MOCK EMAIL - Verification URL: {}/api/auth/v1/verify-email?token={}", baseUrl, verificationToken);
        // Simulate successful email sending
    }

    @Override
    public void sendPasswordResetEmail(String to, String username, String resetToken) {
        // TODO: Implement when Mailgun is configured
        log.info("MOCK EMAIL - Password reset email would be sent to: {} with token: {}", to, resetToken);
        log.info("MOCK EMAIL - Reset URL: {}/reset-password?token={}", frontendUrl, resetToken);
        // Simulate successful email sending
    }

    @Override
    public void sendWelcomeEmail(String to, String username) {
        // TODO: Implement when Mailgun is configured  
        log.info("MOCK EMAIL - Welcome email would be sent to: {} for user: {}", to, username);
        // Simulate successful email sending
    }

    private String buildVerificationEmailTemplate(String username, String verificationToken) {
        String verificationUrl = baseUrl + "/api/auth/v1/verify-email?token=" + verificationToken;
        
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Verify Your Email - LigiOpen</title>
            </head>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px;">
                <div style="background: linear-gradient(135deg, #4CAF50, #45a049); padding: 30px; text-align: center; border-radius: 10px 10px 0 0;">
                    <h1 style="color: white; margin: 0; font-size: 28px;">Welcome to LigiOpen!</h1>
                    <p style="color: white; margin: 10px 0 0 0; font-size: 16px;">Kenya's Premier Football Management Platform</p>
                </div>
                
                <div style="background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px;">
                    <h2 style="color: #333; margin-bottom: 20px;">Hi %s,</h2>
                    
                    <p>Thank you for joining LigiOpen! You're now part of Kenya's growing football community.</p>
                    
                    <p>To complete your registration and start exploring our platform, please verify your email address:</p>
                    
                    <div style="text-align: center; margin: 30px 0;">
                        <a href="%s" 
                           style="background-color: #4CAF50; color: white; padding: 15px 30px; text-decoration: none; border-radius: 5px; display: inline-block; font-weight: bold; font-size: 16px;">
                           Verify My Email
                        </a>
                    </div>
                    
                    <p>Or copy and paste this link into your browser:</p>
                    <p style="word-break: break-all; background: #e9e9e9; padding: 10px; border-radius: 5px; font-size: 14px;">%s</p>
                    
                    <div style="background: #fff3cd; border: 1px solid #ffeaa7; padding: 15px; border-radius: 5px; margin: 20px 0;">
                        <p style="margin: 0; color: #856404;"><strong>⚠️ Important:</strong> This verification link will expire in 24 hours.</p>
                    </div>
                    
                    <p>Once verified, you'll be able to:</p>
                    <ul>
                        <li>✅ Create and manage your club profile</li>
                        <li>✅ Connect with players across Kenya</li>
                        <li>✅ Follow competitions from grassroots to premier league</li>
                        <li>✅ Access live match updates and statistics</li>
                    </ul>
                    
                    <hr style="border: none; border-top: 1px solid #ddd; margin: 30px 0;">
                    
                    <p style="font-size: 14px; color: #666;">
                        If you didn't create this account, please ignore this email.<br>
                        Need help? Contact us at <a href="mailto:support@ligiopen.com">support@ligiopen.com</a>
                    </p>
                    
                    <p style="font-size: 14px; color: #666; margin-top: 20px;">
                        Best regards,<br>
                        <strong>The LigiOpen Team</strong><br>
                        <em>Uniting Kenyan Football Through Technology</em>
                    </p>
                </div>
            </body>
            </html>
            """, username, verificationUrl, verificationUrl);
    }

    private String buildPasswordResetEmailTemplate(String username, String resetToken) {
        String resetUrl = frontendUrl + "/reset-password?token=" + resetToken;
        
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Reset Your Password - LigiOpen</title>
            </head>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px;">
                <div style="background: linear-gradient(135deg, #f44336, #d32f2f); padding: 30px; text-align: center; border-radius: 10px 10px 0 0;">
                    <h1 style="color: white; margin: 0; font-size: 28px;">Password Reset</h1>
                    <p style="color: white; margin: 10px 0 0 0; font-size: 16px;">LigiOpen Account Security</p>
                </div>
                
                <div style="background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px;">
                    <h2 style="color: #333; margin-bottom: 20px;">Hi %s,</h2>
                    
                    <p>We received a request to reset the password for your LigiOpen account.</p>
                    
                    <div style="text-align: center; margin: 30px 0;">
                        <a href="%s" 
                           style="background-color: #f44336; color: white; padding: 15px 30px; text-decoration: none; border-radius: 5px; display: inline-block; font-weight: bold; font-size: 16px;">
                           Reset My Password
                        </a>
                    </div>
                    
                    <p>Or copy and paste this link into your browser:</p>
                    <p style="word-break: break-all; background: #e9e9e9; padding: 10px; border-radius: 5px; font-size: 14px;">%s</p>
                    
                    <div style="background: #f8d7da; border: 1px solid #f5c6cb; padding: 15px; border-radius: 5px; margin: 20px 0;">
                        <p style="margin: 0; color: #721c24;"><strong>⚠️ Security Notice:</strong> This link will expire in 1 hour for security reasons.</p>
                    </div>
                    
                    <p style="font-size: 14px; color: #666;">
                        If you didn't request this password reset, please ignore this email. Your password will remain unchanged.
                    </p>
                    
                    <p style="font-size: 14px; color: #666; margin-top: 20px;">
                        Best regards,<br>
                        <strong>The LigiOpen Team</strong>
                    </p>
                </div>
            </body>
            </html>
            """, username, resetUrl, resetUrl);
    }

    private String buildWelcomeEmailTemplate(String username) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Welcome to LigiOpen!</title>
            </head>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px;">
                <div style="background: linear-gradient(135deg, #4CAF50, #45a049); padding: 30px; text-align: center; border-radius: 10px 10px 0 0;">
                    <h1 style="color: white; margin: 0; font-size: 28px;">🎉 Welcome to LigiOpen!</h1>
                    <p style="color: white; margin: 10px 0 0 0; font-size: 16px;">You're all set to explore Kenyan football!</p>
                </div>
                
                <div style="background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px;">
                    <h2 style="color: #333; margin-bottom: 20px;">Hello %s! 👋</h2>
                    
                    <p>Congratulations! Your email has been verified and your LigiOpen account is now active.</p>
                    
                    <h3 style="color: #4CAF50; margin-top: 30px;">🚀 Get Started:</h3>
                    <ul>
                        <li><strong>Complete Your Profile:</strong> Add your photo and personal information</li>
                        <li><strong>Explore Clubs:</strong> Discover teams from grassroots to premier league</li>
                        <li><strong>Follow Competitions:</strong> Stay updated with live matches and standings</li>
                        <li><strong>Connect with Players:</strong> Build your football network across Kenya</li>
                    </ul>
                    
                    <div style="text-align: center; margin: 30px 0;">
                        <a href="%s" 
                           style="background-color: #4CAF50; color: white; padding: 15px 30px; text-decoration: none; border-radius: 5px; display: inline-block; font-weight: bold; font-size: 16px;">
                           Explore LigiOpen
                        </a>
                    </div>
                    
                    <h3 style="color: #4CAF50; margin-top: 30px;">📱 Need Help?</h3>
                    <p>Our support team is here to help you get the most out of LigiOpen:</p>
                    <ul>
                        <li>📧 Email: <a href="mailto:support@ligiopen.com">support@ligiopen.com</a></li>
                        <li>📞 Phone: +254 700 000 000</li>
                        <li>💬 Live Chat: Available in the app</li>
                    </ul>
                    
                    <p style="font-size: 14px; color: #666; margin-top: 30px;">
                        Thank you for joining our mission to digitize and unite Kenyan football!<br><br>
                        
                        Best regards,<br>
                        <strong>The LigiOpen Team</strong><br>
                        <em>Uniting Kenyan Football Through Technology</em> 🇰🇪⚽
                    </p>
                </div>
            </body>
            </html>
            """, username, frontendUrl);
    }
}