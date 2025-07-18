package com.jabulani.ligiopen.controller.auth;

import com.jabulani.ligiopen.config.response.Response;
import com.jabulani.ligiopen.model.user.dto.LoginUserDto;
import com.jabulani.ligiopen.model.user.dto.RegisterUserDto;
import org.springframework.http.ResponseEntity;

public interface AuthController {
    ResponseEntity<Response> register(RegisterUserDto userRegistrationDetails);
    ResponseEntity<Response> login(LoginUserDto userLoginDetails);
}
