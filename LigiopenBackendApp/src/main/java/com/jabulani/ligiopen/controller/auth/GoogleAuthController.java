package com.jabulani.ligiopen.controller.auth;

import com.jabulani.ligiopen.model.dto.classes.TokenDto;
import org.springframework.http.ResponseEntity;

public interface GoogleAuthController {

    ResponseEntity<Object> googleAuthSuccess(String token);

    ResponseEntity<Object> googleAuthFailure();
}