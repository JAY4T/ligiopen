package com.jabulani.ligiopen.entity.file;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "files")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "stored_name")
    private String storedName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "file_type")
    @Enumerated(EnumType.STRING)
    private FileType fileType;

    @Column(name = "uploaded_by")
    private Long uploadedBy;

    @Column(name = "description")
    private String description;

    @Column(name = "alt_text")
    private String altText;

    @Column(name = "is_public")
    private Boolean isPublic = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum FileType {
        IMAGE, VIDEO, DOCUMENT, AUDIO, OTHER
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}