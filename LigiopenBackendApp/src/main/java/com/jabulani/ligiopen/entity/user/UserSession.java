package com.jabulani.ligiopen.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_sessions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "device_info")
    private String deviceInfo;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;
}