package com.jabulani.ligiopen.controller.auth;

import com.jabulani.ligiopen.model.dto.classes.TokenDto;
import org.springframework.http.ResponseEntity;

public interface GoogleAuthController {

    ResponseEntity<TokenDto> googleAuthSuccess(String token);

    ResponseEntity<TokenDto> googleAuthFailure();
}