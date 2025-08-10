package com.jabulani.ligiopen.config.integration;

import net.sargue.mailgun.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailgunConfig {

    @Value("${mailgun.api-key:}")
    private String apiKey;

    @Value("${mailgun.domain:}")
    private String domain;

    @Value("${mailgun.from-email:noreply@ligiopen.com}")
    private String fromEmail;

    @Value("${mailgun.from-name:LigiOpen Team}")
    private String fromName;

    @Bean
    public net.sargue.mailgun.Configuration mailgunConfiguration() {
        return new net.sargue.mailgun.Configuration()
                .domain(domain)
                .apiKey(apiKey)
                .from(fromName, fromEmail);
    }
}