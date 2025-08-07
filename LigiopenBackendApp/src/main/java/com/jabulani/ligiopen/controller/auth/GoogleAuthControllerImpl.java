package com.jabulani.ligiopen.controller.auth;

import com.jabulani.ligiopen.config.response.BuildResponse;
import com.jabulani.ligiopen.model.dto.classes.TokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/google/")
public class GoogleAuthControllerImpl implements GoogleAuthController {

    private final BuildResponse buildResponse;

    @Autowired
    public GoogleAuthControllerImpl(BuildResponse buildResponse) {
        this.buildResponse = buildResponse;
    }

    @GetMapping("success")
    @Override
    public ResponseEntity<Object> googleAuthSuccess(@RequestParam String token) {
        return buildResponse.success(new TokenDto(token, "Google authentication successful"), "success", null, HttpStatus.OK);
    }

    @GetMapping("failure")
    @Override
    public ResponseEntity<Object> googleAuthFailure() {
        Map<String, Object> errors = new HashMap<>();
        errors.put("authentication", "Google authentication failed");
        return buildResponse.error("failed", errors, HttpStatus.UNAUTHORIZED);
    }

}
