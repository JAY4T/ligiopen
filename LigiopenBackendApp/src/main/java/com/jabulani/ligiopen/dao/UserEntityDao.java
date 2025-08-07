package com.jabulani.ligiopen.dao;

import com.jabulani.ligiopen.model.tables.UserEntity;

import java.util.Optional;

public interface UserEntityDao {
    UserEntity createUser(UserEntity userEntity);
    UserEntity updateUser(UserEntity userEntity);
    UserEntity getUserById(Long id);
    Optional<UserEntity> getUserByEmail(String email);
    Optional<UserEntity> getUserByUsername(String username);
    Optional<UserEntity> getUserByGoogleId(String id);
}
