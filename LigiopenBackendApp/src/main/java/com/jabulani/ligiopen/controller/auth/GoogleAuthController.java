package com.jabulani.ligiopen.controller.auth;

import org.springframework.http.ResponseEntity;

public interface GoogleAuthController {

    ResponseEntity<Object> googleAuthSuccess(String token, String refreshToken, Long expiresIn);

    ResponseEntity<Object> googleAuthFailure();
}