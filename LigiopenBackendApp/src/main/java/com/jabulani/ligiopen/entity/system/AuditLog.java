package com.jabulani.ligiopen.entity.system;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "action")
    private String action;

    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "old_values", columnDefinition = "JSON")
    private String oldValues;

    @Column(name = "new_values", columnDefinition = "JSON")
    private String newValues;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "session_id")
    private String sessionId;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}