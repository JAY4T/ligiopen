package com.jabulani.ligiopen.controller.auth;

import com.jabulani.ligiopen.config.response.BuildResponse;
import com.jabulani.ligiopen.config.response.Response;
import com.jabulani.ligiopen.config.security.JWTGenerator;
import com.jabulani.ligiopen.model.user.dto.LoginUserDto;
import com.jabulani.ligiopen.model.user.dto.RegisterUserDto;
import com.jabulani.ligiopen.service.user.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/")
public class AuthControllerImpl implements AuthController{
    private final BuildResponse buildResponse = new BuildResponse();
    private final UserAccountService userAccountService;
    private final AuthenticationManager authenticationManager;

    private final JWTGenerator jwtGenerator;
    @Autowired
    public AuthControllerImpl(
            UserAccountService userAccountService,
            AuthenticationManager authenticationManager,
            JWTGenerator jwtGenerator
    ) {
        this.userAccountService = userAccountService;
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
    }
    @PostMapping("register")
    @Override
    public ResponseEntity<Response> register(@RequestBody RegisterUserDto userRegistrationDetails) {
        if(userAccountService.existsByEmail(userRegistrationDetails.getEmail())) {
            return buildResponse.createResponse(null, null, "User exists", HttpStatus.CONFLICT);
        }

        return buildResponse.createResponse("user", userAccountService.createUserAccount(userRegistrationDetails), "Account created", HttpStatus.CREATED);
    }
    @PostMapping("login")
    @Override
    public ResponseEntity<Response> login(@RequestBody LoginUserDto userLoginDetails) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginDetails.getEmail(),
                            userLoginDetails.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtGenerator.generateToken(authentication);

            Map<Object, Object> userMap = new HashMap<>();
            userMap.put("user", userAccountService.getUserAccountByEmail(userLoginDetails.getEmail()));
            userMap.put("token", token);

            return buildResponse.createResponse("user", userMap, "user logged in", HttpStatus.OK);

        } catch (AuthenticationException e) {
            return buildResponse.createResponse(null, null, e.toString(), HttpStatus.UNAUTHORIZED);
        }
    }
}
