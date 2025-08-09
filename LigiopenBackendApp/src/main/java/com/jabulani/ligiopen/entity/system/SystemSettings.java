package com.jabulani.ligiopen.entity.system;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "system_settings")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SystemSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "setting_key", unique = true)
    private String settingKey;

    @Column(name = "setting_value", columnDefinition = "TEXT")
    private String settingValue;

    @Column(name = "setting_type")
    @Enumerated(EnumType.STRING)
    private SettingType settingType;

    @Column(name = "description")
    private String description;

    @Column(name = "is_public")
    private Boolean isPublic = false;

    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    @Column(name = "modified_by")
    private Long modifiedBy;

    public enum SettingType {
        STRING, INTEGER, BOOLEAN, JSON, DECIMAL
    }

    @PreUpdate
    @PrePersist
    protected void updateTimestamp() {
        lastModified = LocalDateTime.now();
    }
}