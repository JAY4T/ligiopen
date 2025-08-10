package com.jabulani.ligiopen.dao;

import com.jabulani.ligiopen.entity.user.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class UserEntityDaoImpl implements UserEntityDao {

    private static final Logger logger = LoggerFactory.getLogger(UserEntityDaoImpl.class);

    private final EntityManager entityManager;

    @Autowired
    public UserEntityDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public UserEntity createUser(UserEntity userEntity) {
        try {
            // Set creation timestamp if not already set
            if (userEntity.getCreatedAt() == null) {
                userEntity.setCreatedAt(LocalDateTime.now());
            }
            if (userEntity.getUpdatedAt() == null) {
                userEntity.setUpdatedAt(LocalDateTime.now());
            }

            entityManager.persist(userEntity);
//            entityManager.flush(); // Ensure the entity is persisted immediately

            logger.info("Successfully created user with ID: {}", userEntity.getId());
            return userEntity;
        } catch (Exception e) {
            logger.error("Failed to create user: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create user", e);
        }
    }

    @Transactional
    @Override
    public UserEntity updateUser(UserEntity userEntity) {
        try {
            // Update the timestamp
            userEntity.setUpdatedAt(LocalDateTime.now());

            UserEntity updatedUser = entityManager.merge(userEntity);
            entityManager.flush();

            logger.info("Successfully updated user with ID: {}", updatedUser.getId());
            return updatedUser;
        } catch (Exception e) {
            logger.error("Failed to update user with ID: {}", userEntity.getId(), e);
            throw new RuntimeException("Failed to update user", e);
        }
    }

    @Override
    public UserEntity getUserById(Long id) {
        try {
            TypedQuery<UserEntity> query = entityManager.createQuery(
                    "SELECT u FROM UserEntity u WHERE u.id = :id", UserEntity.class);
            query.setParameter("id", id);

            UserEntity user = query.getSingleResult();
            logger.debug("Found user with ID: {}", id);
            return user;
        } catch (NoResultException e) {
            logger.warn("No user found with ID: {}", id);
            throw new RuntimeException("User not found with ID: " + id);
        } catch (Exception e) {
            logger.error("Error retrieving user with ID: {}", id, e);
            throw new RuntimeException("Error retrieving user", e);
        }
    }

    @Override
    public Optional<UserEntity> getUserByEmail(String email) {
        try {
            TypedQuery<UserEntity> query = entityManager.createQuery(
                    "SELECT u FROM UserEntity u WHERE u.email = :email", UserEntity.class);
            query.setParameter("email", email);

            UserEntity user = query.getSingleResult();
            logger.debug("Found user with email: {}", email);
            return Optional.of(user);
        } catch (NoResultException e) {
            logger.debug("No user found with email: {}", email);
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Error retrieving user with email: {}", email, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserEntity> getUserByGoogleId(String googleId) {
        try {
            TypedQuery<UserEntity> query = entityManager.createQuery(
                    "SELECT u FROM UserEntity u WHERE u.googleId = :googleId", UserEntity.class);
            query.setParameter("googleId", googleId);

            UserEntity user = query.getSingleResult();
            logger.debug("Found user with Google ID: {}", googleId);
            return Optional.of(user);
        } catch (NoResultException e) {
            logger.debug("No user found with Google ID: {}", googleId);
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Error retrieving user with Google ID: {}", googleId, e);
            return Optional.empty();
        }
    }

    // Additional useful methods for user management

    public Optional<UserEntity> getUserByGoogleEmail(String googleEmail) {
        try {
            TypedQuery<UserEntity> query = entityManager.createQuery(
                    "SELECT u FROM UserEntity u WHERE u.googleEmail = :googleEmail", UserEntity.class);
            query.setParameter("googleEmail", googleEmail);

            UserEntity user = query.getSingleResult();
            logger.debug("Found user with Google email: {}", googleEmail);
            return Optional.of(user);
        } catch (NoResultException e) {
            logger.debug("No user found with Google email: {}", googleEmail);
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Error retrieving user with Google email: {}", googleEmail, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserEntity> getUserByUsername(String username) {
        try {
            TypedQuery<UserEntity> query = entityManager.createQuery(
                    "SELECT u FROM UserEntity u WHERE u.username = :username", UserEntity.class);
            query.setParameter("username", username);

            UserEntity user = query.getSingleResult();
            logger.debug("Found user with username: {}", username);
            return Optional.of(user);
        } catch (NoResultException e) {
            logger.debug("No user found with username: {}", username);
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Error retrieving user with username: {}", username, e);
            return Optional.empty();
        }
    }

    public List<UserEntity> getAllUsers() {
        try {
            TypedQuery<UserEntity> query = entityManager.createQuery(
                    "SELECT u FROM UserEntity u ORDER BY u.createdAt DESC", UserEntity.class);

            List<UserEntity> users = query.getResultList();
            logger.debug("Retrieved {} users", users.size());
            return users;
        } catch (Exception e) {
            logger.error("Error retrieving all users", e);
            throw new RuntimeException("Error retrieving users", e);
        }
    }

    public List<UserEntity> getUsersByRole(UserEntity.UserRole role) {
        try {
            TypedQuery<UserEntity> query = entityManager.createQuery(
                    "SELECT u FROM UserEntity u WHERE u.role = :role ORDER BY u.createdAt DESC",
                    UserEntity.class);
            query.setParameter("role", role);

            List<UserEntity> users = query.getResultList();
            logger.debug("Retrieved {} users with role: {}", users.size(), role);
            return users;
        } catch (Exception e) {
            logger.error("Error retrieving users with role: {}", role, e);
            throw new RuntimeException("Error retrieving users by role", e);
        }
    }

    public boolean existsByEmail(String email) {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(u) FROM UserEntity u WHERE u.email = :email", Long.class);
            query.setParameter("email", email);

            Long count = query.getSingleResult();
            return count > 0;
        } catch (Exception e) {
            logger.error("Error checking if user exists with email: {}", email, e);
            return false;
        }
    }

    public boolean existsByGoogleId(String googleId) {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(u) FROM UserEntity u WHERE u.googleId = :googleId", Long.class);
            query.setParameter("googleId", googleId);

            Long count = query.getSingleResult();
            return count > 0;
        } catch (Exception e) {
            logger.error("Error checking if user exists with Google ID: {}", googleId, e);
            return false;
        }
    }

    @Override
    public void deleteUser(Long userId) {
        try {
            UserEntity user = getUserById(userId);
            entityManager.remove(user);
            entityManager.flush();

            logger.info("Successfully deleted user with ID: {}", userId);
        } catch (Exception e) {
            logger.error("Failed to delete user with ID: {}", userId, e);
            throw new RuntimeException("Failed to delete user", e);
        }
    }
    
    @Override
    public boolean existsByUsernameAndNotId(String username, Long userId) {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(u) FROM UserEntity u WHERE u.username = :username AND u.id != :userId", 
                    Long.class);
            query.setParameter("username", username);
            query.setParameter("userId", userId);

            Long count = query.getSingleResult();
            return count > 0;
        } catch (Exception e) {
            logger.error("Error checking if user exists with username: {} excluding ID: {}", username, userId, e);
            return false;
        }
    }
    
    @Override
    public boolean existsByEmailAndNotId(String email, Long userId) {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(u) FROM UserEntity u WHERE u.email = :email AND u.id != :userId", 
                    Long.class);
            query.setParameter("email", email);
            query.setParameter("userId", userId);

            Long count = query.getSingleResult();
            return count > 0;
        } catch (Exception e) {
            logger.error("Error checking if user exists with email: {} excluding ID: {}", email, userId, e);
            return false;
        }
    }
}
