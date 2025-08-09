package com.jabulani.ligiopen.entity.notification;

import com.jabulani.ligiopen.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "message", columnDefinition = "TEXT")
    private String message;
    
    @Column(name = "notification_type")
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    
    @Column(name = "is_read")
    private Boolean isRead = false;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "related_entity_id")
    private Long relatedEntityId;
    
    @Column(name = "related_entity_type")
    private String relatedEntityType;

    @Column(name = "action_url")
    private String actionUrl;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.NORMAL;
    
    public enum NotificationType {
        MATCH_REMINDER, 
        CLUB_INVITATION, 
        TRANSFER_REQUEST, 
        MATCH_RESULT, 
        NEWS, 
        SYSTEM_ANNOUNCEMENT,
        PLAYER_REGISTRATION,
        SEASON_REGISTRATION,
        COMPETITION_UPDATE
    }

    public enum Priority {
        LOW, NORMAL, HIGH, URGENT
    }
}
