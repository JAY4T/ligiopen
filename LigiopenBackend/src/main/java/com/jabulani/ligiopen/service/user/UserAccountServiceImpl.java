package com.jabulani.ligiopen.service.user;

import com.jabulani.ligiopen.dao.club.ClubDao;
import com.jabulani.ligiopen.dao.user.UserAccountDao;
import com.jabulani.ligiopen.model.club.entity.Club;
import com.jabulani.ligiopen.model.user.Role;
import com.jabulani.ligiopen.model.user.dto.*;
import com.jabulani.ligiopen.model.user.entity.UserAccount;
import com.jabulani.ligiopen.model.user.dto.mapper.UserAccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAccountServiceImpl implements UserAccountService{
    private final UserAccountMapper userAccountMapper = new UserAccountMapper();
    private final UserAccountDao userAccountDao;
    private final ClubDao clubDao;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserAccountServiceImpl(
            UserAccountDao userAccountDao,
            ClubDao clubDao,
            PasswordEncoder passwordEncoder
    ) {
        this.userAccountDao = userAccountDao;
        this.clubDao = clubDao;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    @Override
    public UserAccountDto createUserAccount(RegisterUserDto userRegDetails) {
        UserAccount userAccount = UserAccount.builder()
                .username(userRegDetails.getUsername())
                .email(userRegDetails.getEmail())
                .password(passwordEncoder.encode(userRegDetails.getPassword()))
                .role(userRegDetails.getRole())
                .createdAt(LocalDateTime.now().plusHours(3))
                .archived(false)
                .build();
        return userAccountMapper.toUserDto(userAccountDao.createUserAccount(userAccount));
    }
    @Transactional
    @Override
    public UserAccountDto updateUserAccount(UpdateUserDto updatedUserDetails) {
        UserAccount userAccount = userAccountDao.getUserAccountById(updatedUserDetails.getUserId());
        if(!userAccount.getUsername().equalsIgnoreCase(updatedUserDetails.getUsername())) {
            userAccount.setUsername(updatedUserDetails.getUsername());
        }

        if(!userAccount.getEmail().equalsIgnoreCase(updatedUserDetails.getEmail())) {
            userAccount.setUsername(updatedUserDetails.getUsername());
        }
        return userAccountMapper.toUserDto(userAccountDao.updateUserAccount(userAccount));
    }
    @Transactional
    @Override
    public UserAccountDto changeUserPassword(PasswordChangeDto passwordChangeDto) {
        UserAccount userAccount = userAccountDao.getUserAccountById(passwordChangeDto.getUserId());
        userAccount.setPassword(passwordEncoder.encode(passwordChangeDto.getPassword()));
        return userAccountMapper.toUserDto(userAccountDao.updateUserAccount(userAccount));
    }

    @Override
    public UserAccountDto getUserAccountById(Integer userId) {
        return userAccountMapper.toUserDto(userAccountDao.getUserAccountById(userId));
    }

    @Override
    public UserAccountDto getUserAccountByEmail(String email) {
        return userAccountMapper.toUserDto(userAccountDao.getUserAccountByEmail(email));
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userAccountDao.existsByEmail(email);
    }

    @Override
    public List<UserAccountDto> getAllUsers() {
        return userAccountDao.getAllUsers().stream().map(userAccountMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public List<UserAccountDto> getUsers(String username, String role) {
        return userAccountDao.getUsers(username, role).stream().map(userAccountMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserAccountDto getUser(Integer userId) {
        return userAccountMapper.toUserDto(userAccountDao.getUser(userId));
    }

    @Transactional
    @Override
    public UserAccountDto setSuperAdmin(SetSuperAdminDto setSuperAdminDto) {
        UserAccount userAccount =  userAccountDao.getUserAccountById(setSuperAdminDto.getUserId());
        userAccount.setRole(Role.SUPER_ADMIN);
        return userAccountMapper.toUserDto(userAccountDao.updateUserAccount(userAccount));
    }

    @Transactional
    @Override
    public UserAccountDto setContentAdmin(SetContentAdminDto setContentAdminDto) {
        UserAccount userAccount =  userAccountDao.getUserAccountById(setContentAdminDto.getUserId());
        userAccount.setRole(Role.CONTENT_ADMIN);
        return userAccountMapper.toUserDto(userAccountDao.updateUserAccount(userAccount));
    }

    @Transactional
    @Override
    public UserAccountDto setTeamAdmin(SetTeamAdminDto setTeamAdminDto) {
        UserAccount userAccount =  userAccountDao.getUserAccountById(setTeamAdminDto.getUserId());
        Club club = clubDao.getClubById(setTeamAdminDto.getTeamId());
        userAccount.setRole(Role.TEAM_ADMIN);
        userAccount.setManagedClub(null);
        userAccount.setManagedClub(club);

        return userAccountMapper.toUserDto(userAccountDao.updateUserAccount(userAccount));
    }
}
