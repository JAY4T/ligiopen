package com.jabulani.ligiopen.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupRequestDto {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}