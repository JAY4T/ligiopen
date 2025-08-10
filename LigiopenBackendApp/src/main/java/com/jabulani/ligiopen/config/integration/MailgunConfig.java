package com.jabulani.ligiopen.config.integration;

import net.sargue.mailgun.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

// @org.springframework.context.annotation.Configuration - Commented out until Mailgun is configured
public class MailgunConfig {

    @Value("${mailgun.api-key:}")
    private String apiKey;

    @Value("${mailgun.domain:}")
    private String domain;

    @Value("${mailgun.from-email:noreply@ligiopen.com}")
    private String fromEmail;

    @Value("${mailgun.from-name:LigiOpen Team}")
    private String fromName;

    // @Bean - Commented out until Mailgun is configured
    public Configuration mailgunConfiguration() {
        // Return a mock configuration if Mailgun is not configured (development mode)
        if (apiKey == null || apiKey.trim().isEmpty() || domain == null || domain.trim().isEmpty()) {
            return new Configuration()
                    .domain("sandbox-mock.mailgun.org")
                    .apiKey("mock-api-key")
                    .from(fromName, fromEmail);
        }
        
        return new Configuration()
                .domain(domain)
                .apiKey(apiKey)
                .from(fromName, fromEmail);
    }
}