package com.jabulani.ligiopen.dao;

import com.jabulani.ligiopen.entity.user.UserEntity;

import java.util.Optional;

public interface UserEntityDao {
    UserEntity createUser(UserEntity userEntity);
    UserEntity updateUser(UserEntity userEntity);
    UserEntity getUserById(Long id);
    UserEntity getUserByIdOrThrow(Long id);
    Optional<UserEntity> getUserByEmail(String email);
    Optional<UserEntity> getUserByUsername(String username);
    Optional<UserEntity> getUserByGoogleId(String id);
    void deleteUser(Long id);
    boolean existsByUsernameAndNotId(String username, Long userId);
    boolean existsByEmailAndNotId(String email, Long userId);
}
