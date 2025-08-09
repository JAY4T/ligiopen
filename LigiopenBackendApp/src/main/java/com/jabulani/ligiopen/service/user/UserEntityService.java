package com.jabulani.ligiopen.service.user;

import com.jabulani.ligiopen.dto.auth.SignupRequestDto;
import com.jabulani.ligiopen.dto.response.SuccessDto;
import com.jabulani.ligiopen.dto.user.UserDto;
import com.jabulani.ligiopen.entity.user.UserEntity;

public interface UserEntityService {

    // Regular user signup
    SuccessDto createUser(SignupRequestDto signupRequestDto);

    // OAuth2 user creation/update - both variants
    UserEntity createOrUpdateOAuth2User(String googleId, String email, String firstName, String lastName);
    UserEntity createOrUpdateOAuth2User(String googleId, String email, String firstName, String lastName, String profilePictureUrl);

    // User retrieval
    UserDto getUserById(Long id);
    UserDto getUserByEmail(String email);

    // User utility methods
    boolean userExistsByEmail(String email);
    void updateUserProfile(Long userId, String firstName, String lastName);
    void verifyUserEmail(String email);
}
