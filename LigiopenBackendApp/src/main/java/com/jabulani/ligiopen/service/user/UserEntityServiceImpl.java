package com.jabulani.ligiopen.service.user;

import com.jabulani.ligiopen.dao.UserEntityDao;
import com.jabulani.ligiopen.dto.auth.SignupRequestDto;
import com.jabulani.ligiopen.dto.response.SuccessDto;
import com.jabulani.ligiopen.dto.user.UserDto;
import com.jabulani.ligiopen.dto.user.UserProfileDto;
import com.jabulani.ligiopen.dto.user.UpdateUserProfileDto;
import com.jabulani.ligiopen.mapper.SuccessMapper;
import com.jabulani.ligiopen.mapper.UserMapper;
import com.jabulani.ligiopen.entity.user.UserEntity;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserEntityServiceImpl implements UserEntityService {

    private static final Logger logger = LoggerFactory.getLogger(UserEntityServiceImpl.class);

    private final UserEntityDao userEntityDao;
    private final UserMapper userMapper;
    private final SuccessMapper successMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;

    @Autowired
    public UserEntityServiceImpl(
            UserEntityDao userEntityDao,
            UserMapper userMapper,
            SuccessMapper successMapper,
            PasswordEncoder passwordEncoder,
            EmailVerificationService emailVerificationService
    ) {
        this.userEntityDao = userEntityDao;
        this.userMapper = userMapper;
        this.successMapper = successMapper;
        this.passwordEncoder = passwordEncoder;
        this.emailVerificationService = emailVerificationService;
    }

    @Transactional
    @Override
    public SuccessDto createUser(SignupRequestDto signupRequestDto) {
        try {
            logger.info("Creating new user with email: {}", signupRequestDto.getEmail());

            // Check if user already exists
            Optional<UserEntity> existingUser = userEntityDao.getUserByEmail(signupRequestDto.getEmail());
            if (existingUser.isPresent()) {
                logger.warn("User already exists with email: {}", signupRequestDto.getEmail());
                throw new RuntimeException("User already exists with this email");
            }

            // Create new user entity
            UserEntity userEntity = UserEntity.builder()
                    .email(signupRequestDto.getEmail())
                    .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                    .role(UserEntity.UserRole.USER)
                    .emailVerified(false)
                    .accountEnabled(true)
                    .username(signupRequestDto.getUsername())
                    .firstName(signupRequestDto.getFirstName())
                    .lastName(signupRequestDto.getLastName())
                    .phoneNumber(signupRequestDto.getPhoneNumber())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            // Save user to database
            UserEntity savedUser = userEntityDao.createUser(userEntity);
            logger.info("Successfully created user with ID: {} and email: {}",
                    savedUser.getId(), savedUser.getEmail());

            // Send verification email asynchronously
            try {
                emailVerificationService.sendVerificationEmail(savedUser);
                logger.info("Verification email queued for user: {}", savedUser.getEmail());
            } catch (Exception e) {
                logger.error("Failed to send verification email to user: {}, but user creation succeeded", 
                    savedUser.getEmail(), e);
                // Don't fail user creation if email sending fails
            }

            return successMapper.toSuccessDto(true);

        } catch (Exception e) {
            logger.error("Failed to create user with email: {}", signupRequestDto.getEmail(), e);
            throw new RuntimeException("Failed to create user: " + e.getMessage());
        }
    }

    @Transactional
    public UserEntity createOrUpdateOAuth2User(String googleId, String email, String firstName, String lastName) {
        return createOrUpdateOAuth2User(googleId, email, firstName, lastName, null);
    }

    @Transactional
    public UserEntity createOrUpdateOAuth2User(String googleId, String email, String firstName, String lastName, String profilePictureUrl) {
        try {
            logger.info("Creating or updating OAuth2 user with Google ID: {} and email: {}", googleId, email);

            // First try to find by Google ID
            Optional<UserEntity> existingUserByGoogleId = userEntityDao.getUserByGoogleId(googleId);
            if (existingUserByGoogleId.isPresent()) {
                UserEntity user = existingUserByGoogleId.get();
                // Update user information
                user.setGoogleEmail(email);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setUpdatedAt(LocalDateTime.now());
                user.setEmailVerified(true); // OAuth emails are considered verified

                logger.info("Updated existing OAuth2 user: {}", user.getEmail());
                return userEntityDao.updateUser(user);
            }

            // Then try to find by email (in case user signed up normally first)
            Optional<UserEntity> existingUserByEmail = userEntityDao.getUserByEmail(email);
            if (existingUserByEmail.isPresent()) {
                UserEntity user = existingUserByEmail.get();
                // Link Google account to existing user
                user.setGoogleId(googleId);
                user.setGoogleEmail(email);
                user.setFirstName(firstName != null ? firstName : user.getFirstName());
                user.setLastName(lastName != null ? lastName : user.getLastName());
                user.setUpdatedAt(LocalDateTime.now());
                user.setEmailVerified(true);

                logger.info("Linked Google account to existing user: {}", user.getEmail());
                return userEntityDao.updateUser(user);
            }

            // Create new OAuth2 user
            UserEntity newUser = UserEntity.builder()
                    .googleId(googleId)
                    .googleEmail(email)
                    .email(email) // Use Google email as primary email
                    .firstName(firstName)
                    .lastName(lastName)
                    .role(UserEntity.UserRole.USER)
                    .emailVerified(true) // OAuth emails are verified
                    .accountEnabled(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            UserEntity savedUser = userEntityDao.createUser(newUser);
            logger.info("Successfully created new OAuth2 user with ID: {} and email: {}",
                    savedUser.getId(), savedUser.getEmail());

            return savedUser;

        } catch (Exception e) {
            logger.error("Failed to create or update OAuth2 user with Google ID: {} and email: {}",
                    googleId, email, e);
            throw new RuntimeException("Failed to create or update OAuth2 user: " + e.getMessage());
        }
    }

    @Override
    public UserDto getUserById(Long id) {
        try {
            UserEntity user = userEntityDao.getUserById(id);
            return userMapper.toUserDto(user);
        } catch (Exception e) {
            logger.error("Failed to get user by ID: {}", id, e);
            throw new RuntimeException("User not found with ID: " + id);
        }
    }

    public UserDto getUserByEmail(String email) {
        try {
            Optional<UserEntity> user = userEntityDao.getUserByEmail(email);
            if (user.isPresent()) {
                return userMapper.toUserDto(user.get());
            }
            throw new RuntimeException("User not found with email: " + email);
        } catch (Exception e) {
            logger.error("Failed to get user by email: {}", email, e);
            throw new RuntimeException("User not found with email: " + email);
        }
    }

    public boolean userExistsByEmail(String email) {
        try {
            return userEntityDao.getUserByEmail(email).isPresent();
        } catch (Exception e) {
            logger.error("Error checking if user exists by email: {}", email, e);
            return false;
        }
    }

    @Transactional
    public void updateUserProfile(Long userId, String firstName, String lastName) {
        try {
            UserEntity user = userEntityDao.getUserById(userId);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUpdatedAt(LocalDateTime.now());

            userEntityDao.updateUser(user);
            logger.info("Successfully updated profile for user ID: {}", userId);
        } catch (Exception e) {
            logger.error("Failed to update profile for user ID: {}", userId, e);
            throw new RuntimeException("Failed to update user profile: " + e.getMessage());
        }
    }

    @Transactional
    public void verifyUserEmail(String email) {
        try {
            Optional<UserEntity> userOpt = userEntityDao.getUserByEmail(email);
            if (userOpt.isPresent()) {
                UserEntity user = userOpt.get();
                user.setEmailVerified(true);
                user.setUpdatedAt(LocalDateTime.now());
                userEntityDao.updateUser(user);
                logger.info("Email verified for user: {}", email);
            }
        } catch (Exception e) {
            logger.error("Failed to verify email for user: {}", email, e);
            throw new RuntimeException("Failed to verify email: " + e.getMessage());
        }
    }

    // Profile CRUD operations implementation
    
    @Override
    public UserProfileDto getUserProfile(Long userId) {
        try {
            logger.info("Fetching profile for user ID: {}", userId);
            UserEntity user = userEntityDao.getUserById(userId);
            return userMapper.toUserProfileDto(user);
        } catch (Exception e) {
            logger.error("Failed to get profile for user ID: {}", userId, e);
            throw new RuntimeException("Failed to get user profile: " + e.getMessage());
        }
    }
    
    @Transactional
    @Override
    public UserProfileDto updateUserProfile(Long userId, UpdateUserProfileDto updateDto) {
        try {
            logger.info("Updating profile for user ID: {}", userId);
            
            UserEntity user = userEntityDao.getUserById(userId);
            
            // Validate unique constraints before updating
            if (updateDto.getUsername() != null && 
                !updateDto.getUsername().equals(user.getUsername()) &&
                userEntityDao.existsByUsernameAndNotId(updateDto.getUsername(), userId)) {
                throw new RuntimeException("Username is already taken");
            }
            
            if (updateDto.getEmail() != null && 
                !updateDto.getEmail().equals(user.getEmail()) &&
                userEntityDao.existsByEmailAndNotId(updateDto.getEmail(), userId)) {
                throw new RuntimeException("Email is already in use");
            }
            
            // Update user fields if provided
            if (updateDto.getFirstName() != null) {
                user.setFirstName(updateDto.getFirstName());
            }
            if (updateDto.getLastName() != null) {
                user.setLastName(updateDto.getLastName());
            }
            if (updateDto.getPhoneNumber() != null) {
                user.setPhoneNumber(updateDto.getPhoneNumber());
            }
            if (updateDto.getBio() != null) {
                user.setBio(updateDto.getBio());
            }
            if (updateDto.getPreferredLanguage() != null) {
                user.setPreferredLanguage(updateDto.getPreferredLanguage());
            }
            if (updateDto.getEmail() != null) {
                user.setEmail(updateDto.getEmail());
                // Reset email verification if email changed
                user.setEmailVerified(false);
            }
            if (updateDto.getUsername() != null) {
                user.setUsername(updateDto.getUsername());
            }
            
            user.setUpdatedAt(LocalDateTime.now());
            UserEntity updatedUser = userEntityDao.updateUser(user);
            
            logger.info("Successfully updated profile for user ID: {}", userId);
            return userMapper.toUserProfileDto(updatedUser);
        } catch (Exception e) {
            logger.error("Failed to update profile for user ID: {}", userId, e);
            throw new RuntimeException("Failed to update user profile: " + e.getMessage());
        }
    }
    
    @Transactional
    @Override
    public void deleteUserProfile(Long userId) {
        try {
            logger.info("Deleting user profile for user ID: {}", userId);
            
            // Get user to ensure it exists
            UserEntity user = userEntityDao.getUserById(userId);
            logger.info("Found user to delete: {}", user.getEmail());
            
            // Delete the user (this will cascade delete related records)
            userEntityDao.deleteUser(userId);
            
            logger.info("Successfully deleted user profile for user ID: {}", userId);
        } catch (Exception e) {
            logger.error("Failed to delete user profile for user ID: {}", userId, e);
            throw new RuntimeException("Failed to delete user profile: " + e.getMessage());
        }
    }
    
    @Transactional
    @Override
    public UserProfileDto updateProfilePicture(Long userId, Long profilePictureId) {
        try {
            logger.info("Updating profile picture for user ID: {} with picture ID: {}", userId, profilePictureId);
            
            UserEntity user = userEntityDao.getUserById(userId);
            user.setProfilePictureId(profilePictureId);
            user.setUpdatedAt(LocalDateTime.now());
            
            UserEntity updatedUser = userEntityDao.updateUser(user);
            logger.info("Successfully updated profile picture for user ID: {}", userId);
            
            return userMapper.toUserProfileDto(updatedUser);
        } catch (Exception e) {
            logger.error("Failed to update profile picture for user ID: {}", userId, e);
            throw new RuntimeException("Failed to update profile picture: " + e.getMessage());
        }
    }
    
    @Transactional
    @Override
    public void removeProfilePicture(Long userId) {
        try {
            logger.info("Removing profile picture for user ID: {}", userId);
            
            UserEntity user = userEntityDao.getUserById(userId);
            user.setProfilePictureId(null);
            user.setUpdatedAt(LocalDateTime.now());
            
            userEntityDao.updateUser(user);
            logger.info("Successfully removed profile picture for user ID: {}", userId);
        } catch (Exception e) {
            logger.error("Failed to remove profile picture for user ID: {}", userId, e);
            throw new RuntimeException("Failed to remove profile picture: " + e.getMessage());
        }
    }
    
    // Additional utility methods
    
    @Override
    public boolean userExistsByUsername(String username) {
        try {
            return userEntityDao.getUserByUsername(username).isPresent();
        } catch (Exception e) {
            logger.error("Error checking if user exists by username: {}", username, e);
            return false;
        }
    }
    
    @Override
    public boolean userExistsByUsernameAndNotId(String username, Long userId) {
        try {
            return userEntityDao.existsByUsernameAndNotId(username, userId);
        } catch (Exception e) {
            logger.error("Error checking if user exists by username excluding ID: {}", username, e);
            return false;
        }
    }
    
    @Override
    public boolean userExistsByEmailAndNotId(String email, Long userId) {
        try {
            return userEntityDao.existsByEmailAndNotId(email, userId);
        } catch (Exception e) {
            logger.error("Error checking if user exists by email excluding ID: {}", email, e);
            return false;
        }
    }
}
