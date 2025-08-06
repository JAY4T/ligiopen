package com.jabulani.ligiopen.controller.auth;

import com.jabulani.ligiopen.config.security.JWTGenerator;
import com.jabulani.ligiopen.dao.UserEntityDao;
import com.jabulani.ligiopen.model.dto.classes.*;
import com.jabulani.ligiopen.model.tables.UserEntity;
import com.jabulani.ligiopen.model.tables.UserEntity.UserRole;
import com.jabulani.ligiopen.service.userEnity.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth/")
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserEntityDao userEntityDao;
    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator jwtGenerator;
    private final UserEntityService userEntityService;

    @PostMapping("signup")
    @Override
    public ResponseEntity<SuccessDto> registerUser(@RequestBody SignupRequestDto signUpRequest) {
        // Handle Google signup (should be handled by OAuth2 flow)
        if (signUpRequest.getOauthMethod() == OauthMethod.GOOGLE) {
            return ResponseEntity.badRequest()
                    .body(new SuccessDto(false, "Google signup should be handled through OAuth2 flow"));
        }

        // Local signup
        if (userEntityDao.getUserByEmail(signUpRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(new SuccessDto(false, "Error: Email is already in use!"));
        }

        // Create new user's account
        UserEntity user = UserEntity.builder()
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .emailVerified(false)
                .accountEnabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .role(UserRole.USER)
                .build();

        userEntityDao.createUser(user);

        return ResponseEntity.ok(new SuccessDto(true, "User registered successfully!"));
    }

    @PostMapping("signin")
    @Override
    public ResponseEntity<TokenDto> authenticateUser(@RequestBody LoginRequestDto loginRequest) {
        // Handle Google login (should be handled by OAuth2 flow)
        if (loginRequest.getOauthMethod() == OauthMethod.GOOGLE) {
            return ResponseEntity.badRequest()
                    .body(new TokenDto(null, "Google login should be handled through OAuth2 flow"));
        }

        // Local login
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtGenerator.generateToken(authentication);

        return ResponseEntity.ok(new TokenDto(jwt, "Login successful"));
    }
}