package com.jabulani.ligiopen.dao;

import com.jabulani.ligiopen.model.tables.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserEntityDaoImpl implements UserEntityDao{
    private final EntityManager entityManager;

    @Autowired
    public UserEntityDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public UserEntity createUser(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }

    @Override
    public UserEntity updateUser(UserEntity userEntity) {
        entityManager.merge(userEntity);
        return userEntity;
    }

    @Override
    public UserEntity getUserById(Long id) {
        TypedQuery<UserEntity> query = entityManager.createQuery("from UserEntity where id = :id", UserEntity.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public Optional<UserEntity> getUserByEmail(String email) {
        TypedQuery<UserEntity> query = entityManager.createQuery("from UserEntity where email = :email", UserEntity.class);
        query.setParameter("email", email);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public Optional<UserEntity> getUserByGoogleId(String id) {
        TypedQuery<UserEntity> query = entityManager.createQuery("from UserEntity where googleId = :id", UserEntity.class);
        query.setParameter("id", id);
        return Optional.ofNullable(query.getSingleResult());
    }
}
