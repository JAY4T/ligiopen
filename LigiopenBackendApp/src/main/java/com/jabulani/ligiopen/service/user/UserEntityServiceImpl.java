package com.jabulani.ligiopen.service.user;

import com.jabulani.ligiopen.dao.UserEntityDao;
import com.jabulani.ligiopen.dto.auth.SignupRequestDto;
import com.jabulani.ligiopen.dto.response.SuccessDto;
import com.jabulani.ligiopen.dto.user.UserDto;
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
}
