package com.jabulani.ligiopen.dao;

import com.jabulani.ligiopen.model.UserEntity;

import java.util.Optional;

public interface UserEntityDao {
    UserEntity createUser(UserEntity userEntity);
    UserEntity updateUser(UserEntity userEntity);
    Optional<UserEntity> getUserByEmail(String email);
    Optional<UserEntity> getUserByGoogleId(String id);
}
