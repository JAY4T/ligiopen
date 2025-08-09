package com.jabulani.ligiopen.controller.auth;

import com.jabulani.ligiopen.dto.auth.LoginRequestDto;
import com.jabulani.ligiopen.dto.auth.SignupRequestDto;
import com.jabulani.ligiopen.dto.response.SuccessDto;
import com.jabulani.ligiopen.dto.auth.TokenDto;
import org.springframework.http.ResponseEntity;

public interface LocalAuthController {

    ResponseEntity<Object> registerUser(SignupRequestDto signUpRequest);

    ResponseEntity<Object> authenticateUser(LoginRequestDto loginRequest);

    ResponseEntity<Object> refreshToken(java.util.Map<String, String> request);
}