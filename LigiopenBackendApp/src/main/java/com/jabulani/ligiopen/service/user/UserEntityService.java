package com.jabulani.ligiopen.service.user;

import com.jabulani.ligiopen.dto.auth.SignupRequestDto;
import com.jabulani.ligiopen.dto.response.SuccessDto;
import com.jabulani.ligiopen.dto.user.UserDto;
import com.jabulani.ligiopen.dto.user.UserProfileDto;
import com.jabulani.ligiopen.dto.user.UpdateUserProfileDto;
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
    
    // User Profile CRUD operations
    UserProfileDto getUserProfile(Long userId);
    UserProfileDto updateUserProfile(Long userId, UpdateUserProfileDto updateDto);
    void deleteUserProfile(Long userId);
    
    // Profile picture management
    UserProfileDto updateProfilePicture(Long userId, Long profilePictureId);
    void removeProfilePicture(Long userId);

    // User utility methods
    boolean userExistsByEmail(String email);
    void updateUserProfile(Long userId, String firstName, String lastName);
    void verifyUserEmail(String email);
    
    // User existence checks
    boolean userExistsByUsername(String username);
    boolean userExistsByUsernameAndNotId(String username, Long userId);
    boolean userExistsByEmailAndNotId(String email, Long userId);
}
