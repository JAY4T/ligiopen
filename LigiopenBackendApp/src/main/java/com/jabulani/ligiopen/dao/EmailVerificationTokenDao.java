package com.jabulani.ligiopen.dao;

import com.jabulani.ligiopen.entity.user.EmailVerificationToken;
import com.jabulani.ligiopen.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmailVerificationTokenDao extends JpaRepository<EmailVerificationToken, Long> {
    
    Optional<EmailVerificationToken> findByTokenAndUsedFalse(String token);
    
    Optional<EmailVerificationToken> findByUserAndUsedFalse(UserEntity user);
    
    List<EmailVerificationToken> findByExpiryDateBeforeAndUsedFalse(LocalDateTime dateTime);
    
    void deleteByUser(UserEntity user);
}