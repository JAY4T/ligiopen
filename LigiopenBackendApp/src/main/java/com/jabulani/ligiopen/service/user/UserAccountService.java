package com.jabulani.ligiopen.service.user;

import com.jabulani.ligiopen.model.user.dto.*;

import java.util.List;

public interface UserAccountService {
    UserAccountDto createUserAccount(RegisterUserDto userAccount);
    UserAccountDto updateUserAccount(UpdateUserDto userAccount);
    UserAccountDto changeUserPassword(PasswordChangeDto passwordChangeDto);
    UserAccountDto getUserAccountById(Integer userId);
    UserAccountDto getUserAccountByEmail(String email);
    Boolean existsByEmail(String email);
    List<UserAccountDto> getAllUsers();
    List<UserAccountDto> getUsers(String username, String role);
    UserAccountDto getUser(Integer userId);

    UserAccountDto setSuperAdmin(SetSuperAdminDto setSuperAdminDto);
    UserAccountDto setContentAdmin(SetContentAdminDto setContentAdminDto);
    UserAccountDto setTeamAdmin(SetTeamAdminDto setTeamAdminDto);
}
