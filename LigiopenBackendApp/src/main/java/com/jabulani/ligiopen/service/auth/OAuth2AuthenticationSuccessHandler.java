package com.jabulani.ligiopen.service.auth;

import com.jabulani.ligiopen.config.security.JWTGenerator;
import com.jabulani.ligiopen.config.security.CustomUserDetailsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTGenerator jwtGenerator;
    private final CustomUserDetailsService userDetailsService;

    public OAuth2AuthenticationSuccessHandler(JWTGenerator jwtGenerator,
                                              CustomUserDetailsService userDetailsService) {
        this.jwtGenerator = jwtGenerator;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        try {
            logger.debug("OAuth2 Authentication Success Triggered");

            // 1. Verify authentication object
            if (authentication == null) {
                logger.error("Authentication object is null");
                response.sendRedirect("/api/auth/google/failure");
                return;
            }

            // 2. Verify principal
            Object principal = authentication.getPrincipal();
            if (!(principal instanceof CustomOAuth2User)) {
                logger.error("Principal is not CustomOAuth2User. Actual type: " +
                        (principal != null ? principal.getClass() : "null"));
                response.sendRedirect("/api/auth/google/failure");
                return;
            }

            CustomOAuth2User oauthUser = (CustomOAuth2User) principal;
            logger.info("Authenticated user email: " + oauthUser.getEmail());

            // 3. Verify JWT generation
            String jwtToken = jwtGenerator.generateToken(authentication);
            if (jwtToken == null) {
                logger.error("JWT token generation failed");
                response.sendRedirect("/api/auth/google/failure");
                return;
            }

            logger.debug("Generated JWT: " + jwtToken);

            String targetUrl = UriComponentsBuilder.fromUriString("/api/auth/google/success")
                    .queryParam("token", jwtToken)
                    .build().toUriString();

            logger.debug("Redirecting to: " + targetUrl);
            getRedirectStrategy().sendRedirect(request, response, targetUrl);

        } catch (Exception e) {
            logger.error("OAuth2 processing failed", e);
            response.sendRedirect("/api/auth/google/failure");
        }
    }
}