package com.jabulani.ligiopen.service.userEnity;

import com.jabulani.ligiopen.model.dto.classes.SignupRequestDto;
import com.jabulani.ligiopen.model.dto.classes.SuccessDto;
import com.jabulani.ligiopen.model.dto.classes.UserDto;
import com.jabulani.ligiopen.model.tables.UserEntity;

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
