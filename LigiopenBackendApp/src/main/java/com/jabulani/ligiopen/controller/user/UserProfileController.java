package com.jabulani.ligiopen.controller.user;

import com.jabulani.ligiopen.dto.user.UpdateUserProfileDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UserProfileController {

    ResponseEntity<Object> getCurrentUserProfile();

    ResponseEntity<Object> getUserProfile(Long userId);

    ResponseEntity<Object> updateCurrentUserProfile(UpdateUserProfileDto updateDto);

    ResponseEntity<Object> deleteCurrentUserProfile();

    ResponseEntity<Object> uploadProfilePicture(MultipartFile file);

    ResponseEntity<Object> removeProfilePicture();
}