package com.jabulani.ligiopen.controller.auth;

import com.jabulani.ligiopen.model.dto.classes.TokenDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/google/")
public class GoogleAuthControllerImpl implements GoogleAuthController {

    @GetMapping("success")
    @Override
    public ResponseEntity<TokenDto> googleAuthSuccess(@RequestParam String token) {
        return ResponseEntity.ok(new TokenDto(token, "Google authentication successful"));
    }

    @GetMapping("failure")
    @Override
    public ResponseEntity<TokenDto> googleAuthFailure() {
        return ResponseEntity.badRequest()
                .body(new TokenDto(null, "Google authentication failed"));
    }

}
