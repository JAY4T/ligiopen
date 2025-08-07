package com.jabulani.ligiopen.controller.auth;

import com.jabulani.ligiopen.model.dto.classes.LoginRequestDto;
import com.jabulani.ligiopen.model.dto.classes.SignupRequestDto;
import com.jabulani.ligiopen.model.dto.classes.SuccessDto;
import com.jabulani.ligiopen.model.dto.classes.TokenDto;
import org.springframework.http.ResponseEntity;

public interface LocalAuthController {

    ResponseEntity<Object> registerUser(SignupRequestDto signUpRequest);

    ResponseEntity<Object> authenticateUser(LoginRequestDto loginRequest);
}