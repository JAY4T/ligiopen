package com.jabulani.ligiopen.service.auth;

import com.jabulani.ligiopen.config.security.JWTGenerator;
import com.jabulani.ligiopen.config.security.CustomUserDetailsService;
import com.jabulani.ligiopen.model.tables.UserEntity;
import com.jabulani.ligiopen.service.userEnity.UserEntityService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

// Remove @Component annotation - this class will be managed by @Bean method
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);

    private final JWTGenerator jwtGenerator;
    private final CustomUserDetailsService userDetailsService;
    private final UserEntityService userEntityService;

    // Constructor injection with all dependencies
    public OAuth2AuthenticationSuccessHandler(JWTGenerator jwtGenerator,
                                              CustomUserDetailsService userDetailsService,
                                              UserEntityService userEntityService) {
        this.jwtGenerator = jwtGenerator;
        this.userDetailsService = userDetailsService;
        this.userEntityService = userEntityService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        try {
            logger.info("OAuth2 authentication successful for user: {}", authentication.getName());

            // Check if the principal is OAuth2User
            if (!(authentication.getPrincipal() instanceof OAuth2User)) {
                logger.error("Expected OAuth2User but got: {}", authentication.getPrincipal().getClass());
                response.sendRedirect("/api/auth/google/failure");
                return;
            }

            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

            // Extract user information from OAuth2User
            String email = extractEmail(oauth2User);
            String googleId = extractGoogleId(oauth2User);
            String firstName = extractFirstName(oauth2User);
            String lastName = extractLastName(oauth2User);
            String profilePictureUrl = extractProfilePictureUrl(oauth2User);

            if (email == null || googleId == null) {
                logger.error("Missing required OAuth2 user data - email: {}, googleId: {}", email, googleId);
                response.sendRedirect("/api/auth/google/failure");
                return;
            }

            // **THIS IS WHERE THE USER SAVING LOGIC HAPPENS**
            UserEntity savedUser;
            try {
                // Create or update the OAuth2 user in the database
                savedUser = userEntityService.createOrUpdateOAuth2User(googleId, email, firstName, lastName, profilePictureUrl);
                logger.info("Successfully processed OAuth2 user: {} with ID: {}", email, savedUser.getId());
            } catch (Exception e) {
                logger.error("Failed to save/update OAuth2 user: {}", email, e);
                response.sendRedirect("/api/auth/google/failure");
                return;
            }

            // Generate JWT token using the authentication object
            String jwt;
            try {
                jwt = jwtGenerator.generateToken(authentication);
                logger.info("JWT token generated successfully for user: {}", email);
            } catch (Exception e) {
                logger.error("Failed to generate JWT token for user: {}", email, e);
                response.sendRedirect("/api/auth/google/failure");
                return;
            }

            // Build redirect URL with token and user info
            String redirectUrl = UriComponentsBuilder.fromUriString("/api/auth/google/success")
                    .queryParam("token", jwt)
                    .queryParam("userId", savedUser.getId())
                    .queryParam("email", savedUser.getEmail())
                    .queryParam("isNewUser", savedUser.getCreatedAt().equals(savedUser.getUpdatedAt())) // Check if just created
                    .build().toUriString();

            logger.info("Redirecting to success URL: {}", redirectUrl);
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);

        } catch (Exception e) {
            logger.error("Failed to handle OAuth2 success", e);
            response.sendRedirect("/api/auth/google/failure");
        }
    }

    private String extractEmail(OAuth2User oauth2User) {
        // For CustomOAuth2User
        if (oauth2User instanceof CustomOAuth2User) {
            CustomOAuth2User customUser = (CustomOAuth2User) oauth2User;
            String email = customUser.getEmail();
            if (email != null) return email;
            return customUser.getGoogleEmail();
        }

        // For standard OAuth2User
        return oauth2User.getAttribute("email");
    }

    private String extractGoogleId(OAuth2User oauth2User) {
        // Google ID is usually in the "sub" attribute
        String googleId = oauth2User.getAttribute("sub");
        if (googleId == null) {
            // Fallback to "id" attribute
            googleId = oauth2User.getAttribute("id");
        }
        return googleId;
    }

    private String extractFirstName(OAuth2User oauth2User) {
        String firstName = oauth2User.getAttribute("given_name");
        if (firstName == null) {
            // Fallback to parsing the name
            String fullName = oauth2User.getAttribute("name");
            if (fullName != null && fullName.contains(" ")) {
                firstName = fullName.split(" ")[0];
            }
        }
        return firstName;
    }

    private String extractLastName(OAuth2User oauth2User) {
        String lastName = oauth2User.getAttribute("family_name");
        if (lastName == null) {
            // Fallback to parsing the name
            String fullName = oauth2User.getAttribute("name");
            if (fullName != null && fullName.contains(" ")) {
                String[] nameParts = fullName.split(" ");
                if (nameParts.length > 1) {
                    lastName = nameParts[nameParts.length - 1];
                }
            }
        }
        return lastName;
    }

    private String extractProfilePictureUrl(OAuth2User oauth2User) {
        return oauth2User.getAttribute("picture");
    }
}