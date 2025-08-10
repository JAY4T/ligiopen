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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth/")
public class LocalAuthControllerImpl implements LocalAuthController {

    private final AuthenticationManager authenticationManager;
    private final UserEntityDao userEntityDao;
    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator jwtGenerator;
    private final UserEntityService userEntityService;
    private final EmailVerificationService emailVerificationService;
    private final BuildResponse buildResponse;

    @Autowired
    public LocalAuthControllerImpl(
            AuthenticationManager authenticationManager,
            UserEntityDao userEntityDao,
            PasswordEncoder passwordEncoder,
            JWTGenerator jwtGenerator,
            UserEntityService userEntityService,
            EmailVerificationService emailVerificationService,
            BuildResponse buildResponse
    ) {
        this.authenticationManager = authenticationManager;
        this.userEntityDao = userEntityDao;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.userEntityService = userEntityService;
        this.emailVerificationService = emailVerificationService;
        this.buildResponse = buildResponse;
    }

    @PostMapping("signup")
    @Override
    public ResponseEntity<Object> registerUser(@RequestBody SignupRequestDto signUpRequest) {

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

    @PostMapping("signin")
    @Override
    public ResponseEntity<Object> authenticateUser(@RequestBody LoginRequestDto loginRequest) {

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

    @PostMapping("refresh")
    @Override
    public ResponseEntity<Object> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        
        if (refreshToken == null || refreshToken.isEmpty()) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("refreshToken", "Refresh token is required");
            return buildResponse.error("failed", errors, HttpStatus.BAD_REQUEST);
        }

        try {
            // Validate the refresh token
            if (!jwtGenerator.validateToken(refreshToken) || !jwtGenerator.isRefreshToken(refreshToken)) {
                Map<String, Object> errors = new HashMap<>();
                errors.put("refreshToken", "Invalid or expired refresh token");
                return buildResponse.error("failed", errors, HttpStatus.UNAUTHORIZED);
            }

            // Extract username from refresh token
            String username = jwtGenerator.getUsernameFromJWT(refreshToken);
            
            // Create new authentication object for token generation
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, null)
            );

            // Generate new tokens
            String newAccessToken = jwtGenerator.generateToken(authentication);
            String newRefreshToken = jwtGenerator.generateRefreshToken(authentication);
            Long expiresIn = jwtGenerator.getExpirationMs() / 1000;

            TokenDto tokenResponse = new TokenDto(newAccessToken, newRefreshToken, "Token refreshed successfully", expiresIn);
            return buildResponse.success(tokenResponse, "success", null, HttpStatus.OK);

        } catch (Exception e) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("refreshToken", "Failed to refresh token");
            return buildResponse.error("failed", errors, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("verify-email")
    @Override
    public ResponseEntity<Object> verifyEmail(@RequestParam("token") String token) {
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

    @PostMapping("resend-verification")
    @Override
    public ResponseEntity<Object> resendVerification(@RequestBody ResendVerificationDto request) {
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