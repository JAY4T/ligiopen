package com.jabulani.ligiopen.controller.auth;

import com.jabulani.ligiopen.config.response.BuildResponse;
import com.jabulani.ligiopen.config.security.JWTGenerator;
import com.jabulani.ligiopen.dao.UserEntityDao;
import com.jabulani.ligiopen.model.dto.classes.*;
import com.jabulani.ligiopen.model.tables.UserEntity;
import com.jabulani.ligiopen.model.tables.UserEntity.UserRole;
import com.jabulani.ligiopen.service.userEnity.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/v1/")
public class LocalAuthControllerImpl implements LocalAuthController {

    private final AuthenticationManager authenticationManager;
    private final UserEntityDao userEntityDao;
    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator jwtGenerator;
    private final UserEntityService userEntityService;
    private final BuildResponse buildResponse;

    @Autowired
    public LocalAuthControllerImpl(
            AuthenticationManager authenticationManager,
            UserEntityDao userEntityDao,
            PasswordEncoder passwordEncoder,
            JWTGenerator jwtGenerator,
            UserEntityService userEntityService,
            BuildResponse buildResponse
    ) {
        this.authenticationManager = authenticationManager;
        this.userEntityDao = userEntityDao;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.userEntityService = userEntityService;
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
            // Local login
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            identifier,
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtGenerator.generateToken(authentication);

            return buildResponse.success(new TokenDto(jwt, "Login successful"), "success", null, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("authentication", "Invalid credentials");
            return buildResponse.error("failed", errors, HttpStatus.UNAUTHORIZED);
        }
    }
}