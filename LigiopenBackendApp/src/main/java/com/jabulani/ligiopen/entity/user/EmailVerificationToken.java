package com.jabulani.ligiopen.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_verification_tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailVerificationToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "token", nullable = false, unique = true)
    private String token;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    
    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "used", nullable = false)
    @Builder.Default
    private Boolean used = false;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        // Token expires in 24 hours
        expiryDate = LocalDateTime.now().plusHours(24);
    }
    
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}