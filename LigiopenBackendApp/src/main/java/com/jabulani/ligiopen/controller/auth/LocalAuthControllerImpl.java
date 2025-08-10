package com.jabulani.ligiopen.controller.auth;

import com.jabulani.ligiopen.config.web.BuildResponse;
import com.jabulani.ligiopen.config.security.JWTGenerator;
import com.jabulani.ligiopen.dao.UserEntityDao;
import com.jabulani.ligiopen.dto.auth.LoginRequestDto;
import com.jabulani.ligiopen.dto.auth.ResendVerificationDto;
import com.jabulani.ligiopen.dto.auth.SignupRequestDto;
import com.jabulani.ligiopen.dto.auth.TokenDto;
import com.jabulani.ligiopen.service.user.EmailVerificationService;
import com.jabulani.ligiopen.service.user.UserEntityService;
import com.jabulani.ligiopen.config.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth/")
@Tag(name = "Authentication", description = "User authentication and authorization endpoints")
public class LocalAuthControllerImpl implements LocalAuthController {

    private static final Logger logger = LoggerFactory.getLogger(LocalAuthControllerImpl.class);

    private final AuthenticationManager authenticationManager;
    private final UserEntityDao userEntityDao;
    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator jwtGenerator;
    private final UserEntityService userEntityService;
    private final EmailVerificationService emailVerificationService;
    private final CustomUserDetailsService userDetailsService;
    private final BuildResponse buildResponse;

    @Autowired
    public LocalAuthControllerImpl(
            AuthenticationManager authenticationManager,
            UserEntityDao userEntityDao,
            PasswordEncoder passwordEncoder,
            JWTGenerator jwtGenerator,
            UserEntityService userEntityService,
            EmailVerificationService emailVerificationService,
            CustomUserDetailsService userDetailsService,
            BuildResponse buildResponse
    ) {
        this.authenticationManager = authenticationManager;
        this.userEntityDao = userEntityDao;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.userEntityService = userEntityService;
        this.emailVerificationService = emailVerificationService;
        this.userDetailsService = userDetailsService;
        this.buildResponse = buildResponse;
    }

    @Operation(summary = "Register new user", description = "Create a new user account with email verification")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input or user already exists")
    })
    @PostMapping("signup")
    @Override
    public ResponseEntity<Object> registerUser(
            @Parameter(description = "User registration details", required = true)
            @Valid @RequestBody SignupRequestDto signUpRequest) {

        // Local signup
        if (userEntityDao.getUserByEmail(signUpRequest.getEmail()).isPresent()) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("email", "Error: Email is already in use!");
            return buildResponse.error("failed", errors, HttpStatus.BAD_REQUEST);
        }

        if (userEntityDao.getUserByUsername(signUpRequest.getUsername()).isPresent()) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("username", "Error: Username is already in use!");
            return buildResponse.error("failed", errors, HttpStatus.BAD_REQUEST);
        }

        try {
            return buildResponse.success(userEntityService.createUser(signUpRequest),"Account created",  null, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("exception", e.getMessage());
            return buildResponse.error("failed", errors, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Login user", description = "Authenticate user with email/username and password")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful, JWT tokens returned"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("signin")
    @Override
    public ResponseEntity<Object> authenticateUser(
            @Parameter(description = "Login credentials (email/username and password)", required = true)
            @Valid @RequestBody LoginRequestDto loginRequest) {

        // Determine which identifier to use (email or username)
        String identifier = loginRequest.getEmail() != null && !loginRequest.getEmail().isEmpty()
                ? loginRequest.getEmail()
                : loginRequest.getUsername();

        if (identifier == null || identifier.isEmpty()) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("credential", "Error: Either email or username must be provided!");
            return buildResponse.error("failed", errors, HttpStatus.BAD_REQUEST);
        }

        try {
            // Local login - UserDetailsService will handle email or username lookup
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            identifier,
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtGenerator.generateToken(authentication);
            String refreshToken = jwtGenerator.generateRefreshToken(authentication);
            Long expiresIn = jwtGenerator.getExpirationMs() / 1000; // Convert to seconds

            TokenDto tokenResponse = new TokenDto(jwt, refreshToken, "Login successful", expiresIn);
            return buildResponse.success(tokenResponse, "success", null, HttpStatus.OK);

        } catch (Exception e) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("authentication", "Invalid credentials");
            return buildResponse.error("failed", errors, HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Refresh JWT token", description = "Get new access token using valid refresh token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
    })
    @PostMapping("refresh")
    @Override
    public ResponseEntity<Object> refreshToken(
            @Parameter(description = "Refresh token request body", required = true)
            @RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        logger.info("Refresh token request received");
        
        if (refreshToken == null || refreshToken.isEmpty()) {
            logger.warn("Refresh token is missing from request");
            Map<String, Object> errors = new HashMap<>();
            errors.put("refreshToken", "Refresh token is required");
            return buildResponse.error("failed", errors, HttpStatus.BAD_REQUEST);
        }

        try {
            logger.info("Validating refresh token");
            
            // Validate the refresh token
            boolean isValid = jwtGenerator.validateToken(refreshToken);
            boolean isRefresh = jwtGenerator.isRefreshToken(refreshToken);
            logger.info("Token validation - isValid: {}, isRefresh: {}", isValid, isRefresh);
            
            if (!isValid || !isRefresh) {
                logger.warn("Invalid or expired refresh token");
                Map<String, Object> errors = new HashMap<>();
                errors.put("refreshToken", "Invalid or expired refresh token");
                return buildResponse.error("failed", errors, HttpStatus.UNAUTHORIZED);
            }

            // Extract username from refresh token
            logger.info("Extracting username from refresh token");
            String username = jwtGenerator.getUsernameFromJWT(refreshToken);
            logger.info("Extracted username: {}", username);
            
            // Load user details and create authenticated token
            logger.info("Loading user details for username: {}", username);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            logger.info("User details loaded successfully. Authorities: {}", userDetails.getAuthorities());
            
            // Create pre-authenticated token - this constructor automatically sets authenticated=true
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            logger.info("Authentication object created");

            // Generate new tokens
            logger.info("Generating new tokens");
            String newAccessToken = jwtGenerator.generateToken(authentication);
            String newRefreshToken = jwtGenerator.generateRefreshToken(authentication);
            Long expiresIn = jwtGenerator.getExpirationMs() / 1000;
            logger.info("New tokens generated successfully");

            TokenDto tokenResponse = new TokenDto(newAccessToken, newRefreshToken, "Token refreshed successfully", expiresIn);
            logger.info("Refresh token operation completed successfully");
            return buildResponse.success(tokenResponse, "success", null, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Failed to refresh token", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("refreshToken", "Failed to refresh token: " + e.getMessage());
            return buildResponse.error("failed", errors, HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Verify email address", description = "Verify user's email address using verification token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Email verified successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid or expired verification token")
    })
    @GetMapping("verify-email")
    @Override
    public ResponseEntity<Object> verifyEmail(
            @Parameter(description = "Email verification token", required = true)
            @RequestParam("token") String token) {
        if (token == null || token.trim().isEmpty()) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("token", "Verification token is required");
            return buildResponse.error("failed", errors, HttpStatus.BAD_REQUEST);
        }

        try {
            boolean verified = emailVerificationService.verifyEmail(token);
            
            if (verified) {
                return buildResponse.success(
                    Map.of("verified", true), 
                    "Email verified successfully! Welcome to LigiOpen.", 
                    null, 
                    HttpStatus.OK
                );
            } else {
                Map<String, Object> errors = new HashMap<>();
                errors.put("token", "Invalid or expired verification token");
                return buildResponse.error("failed", errors, HttpStatus.BAD_REQUEST);
            }
            
        } catch (Exception e) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("verification", "Email verification failed");
            return buildResponse.error("failed", errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Resend verification email", description = "Send a new verification email to user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verification email sent successfully"),
        @ApiResponse(responseCode = "400", description = "User not found or already verified")
    })
    @PostMapping("resend-verification")
    @Override
    public ResponseEntity<Object> resendVerification(
            @Parameter(description = "Email address to resend verification", required = true)
            @Valid @RequestBody ResendVerificationDto request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("email", "Email address is required");
            return buildResponse.error("failed", errors, HttpStatus.BAD_REQUEST);
        }

        try {
            boolean sent = emailVerificationService.resendVerificationEmail(request.getEmail());
            
            if (sent) {
                return buildResponse.success(
                    Map.of("sent", true), 
                    "Verification email sent successfully. Please check your inbox.", 
                    null, 
                    HttpStatus.OK
                );
            } else {
                Map<String, Object> errors = new HashMap<>();
                errors.put("email", "User not found or already verified");
                return buildResponse.error("failed", errors, HttpStatus.BAD_REQUEST);
            }
            
        } catch (Exception e) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("email", "Failed to send verification email");
            return buildResponse.error("failed", errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}