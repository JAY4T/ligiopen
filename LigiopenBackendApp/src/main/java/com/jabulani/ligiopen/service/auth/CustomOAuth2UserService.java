package com.jabulani.ligiopen.service.auth;

import com.jabulani.ligiopen.dao.UserEntityDao;
import com.jabulani.ligiopen.model.tables.UserEntity;
import com.jabulani.ligiopen.model.tables.UserEntity.UserRole;
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

    private final UserEntityDao userEntityDao;

    @Autowired
    public CustomOAuth2UserService(UserEntityDao userEntityDao) {
        this.userEntityDao = userEntityDao;
    }

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String googleId = (String) attributes.get("sub");
        String pictureUrl = (String) attributes.get("picture");

        // Split name into first and last names
        String[] names = name.split(" ", 2);
        String firstName = names[0];
        String lastName = names.length > 1 ? names[1] : "";

        UserEntity user = userEntityDao.getUserByGoogleId(googleId)
                .or(() -> userEntityDao.getUserByEmail(email))
                .orElseGet(() -> {
                    UserEntity newUser = UserEntity.builder()
                            .googleId(googleId)
                            .googleEmail(email)
                            .email(email)
                            .emailVerified(true)
                            .accountEnabled(true)
                            .firstName(firstName)
                            .lastName(lastName)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .role(UserRole.USER)
                            .build();
                    return userEntityDao.createUser(newUser);
                });

        // Update user details if they've changed in Google
        if (!user.getGoogleEmail().equals(email) ||
                !user.getFirstName().equals(firstName) ||
                !user.getLastName().equals(lastName)) {

            user.setGoogleEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUpdatedAt(LocalDateTime.now());
            userEntityDao.updateUser(user);
        }

        return new CustomOAuth2User(user, attributes);
    }
}