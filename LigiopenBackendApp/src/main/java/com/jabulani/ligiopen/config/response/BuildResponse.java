package com.jabulani.ligiopen.config.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class BuildResponse {
    public ResponseEntity<Response> createResponse(String desc, Object data, String message, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(Response.builder()
                        .timestamp(System.currentTimeMillis())
                        .data(data) // Directly use the passed data
                        .message(message)
                        .status(status)
                        .statusCode(status.value())
                        .build());
    }
}
