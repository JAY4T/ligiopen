package com.jabulani.ligiopen.service.auth;

import com.jabulani.ligiopen.dao.UserEntityDao;
import com.jabulani.ligiopen.model.tables.UserEntity;
import com.jabulani.ligiopen.model.tables.UserEntity.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    private final UserEntityDao userEntityDao;

    @Autowired
    public CustomOAuth2UserService(UserEntityDao userEntityDao) {
        this.userEntityDao = userEntityDao;
    }

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            OAuth2User oAuth2User = super.loadUser(userRequest);

            // Extract user info from Google
            Map<String, Object> attributes = oAuth2User.getAttributes();

            // Log the attributes for debugging
            logger.debug("OAuth2 user attributes: {}", attributes);

            String email = (String) attributes.get("email");
            String googleId = (String) attributes.get("sub");
            String name = (String) attributes.get("name");
            String givenName = (String) attributes.get("given_name");
            String familyName = (String) attributes.get("family_name");
            String picture = (String) attributes.get("picture");
            Boolean emailVerified = (Boolean) attributes.get("email_verified");

            // Validate required fields
            if (googleId == null || email == null) {
                logger.error("Missing required OAuth2 user information: googleId={}, email={}", googleId, email);
                throw new OAuth2AuthenticationException("Missing required user information");
            }

            // Find or create user in your database
            UserEntity user = userEntityDao.getUserByGoogleId(googleId)
                    .orElseGet(() -> {
                        logger.info("Creating new user for Google ID: {}", googleId);
                        UserEntity newUser = new UserEntity();
                        newUser.setGoogleId(googleId);
                        newUser.setGoogleEmail(email);
                        newUser.setEmail(email);
                        newUser.setFirstName(givenName);
                        newUser.setLastName(familyName);
                        // Set default role if not specified
                        newUser.setRole(UserRole.USER); // Adjust based on your enum values
                        newUser.setEmailVerified(emailVerified != null ? emailVerified : false);
                        newUser.setAccountEnabled(true);
                        newUser.setCreatedAt(LocalDateTime.now());
                        newUser.setUpdatedAt(LocalDateTime.now());

                        return userEntityDao.createUser(newUser);
                    });

            // Update existing user information if needed
            if (user.getId() != null) {
                boolean needsUpdate = false;

                if (!email.equals(user.getGoogleEmail())) {
                    user.setGoogleEmail(email);
                    needsUpdate = true;
                }

                if (givenName != null && !givenName.equals(user.getFirstName())) {
                    user.setFirstName(givenName);
                    needsUpdate = true;
                }

                if (familyName != null && !familyName.equals(user.getLastName())) {
                    user.setLastName(familyName);
                    needsUpdate = true;
                }

                if (needsUpdate) {
                    user.setUpdatedAt(LocalDateTime.now());
                    user = userEntityDao.updateUser(user);
                    logger.info("Updated user information for Google ID: {}", googleId);
                }
            }

            return new CustomOAuth2User(user, attributes);

        } catch (Exception e) {
            logger.error("Error loading OAuth2 user", e);
            throw new OAuth2AuthenticationException("Failed to load user information");
        }
    }
}