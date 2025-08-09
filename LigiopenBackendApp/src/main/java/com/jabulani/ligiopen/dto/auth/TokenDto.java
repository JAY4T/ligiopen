package com.jabulani.ligiopen.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenDto {
    private String accessToken;
    private String message;
    private String tokenType = "Bearer";
    private Long expiresIn;
    private String refreshToken;
    
    // Constructor for simple token response (used by existing controllers)
    public TokenDto(String accessToken, String message) {
        this.accessToken = accessToken;
        this.message = message;
        this.tokenType = "Bearer";
    }
    
    // Constructor for full token response with refresh token
    public TokenDto(String accessToken, String refreshToken, String message, Long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.message = message;
        this.tokenType = "Bearer";
        this.expiresIn = expiresIn;
    }
}