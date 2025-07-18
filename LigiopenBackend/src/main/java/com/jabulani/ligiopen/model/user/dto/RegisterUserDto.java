package com.jabulani.ligiopen.model.user.dto;

import com.jabulani.ligiopen.model.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUserDto {
    private String username;
    private String email;
    private String password;
    private Role role;
}
