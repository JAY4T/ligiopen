package com.jabulani.ligiopen.entity.club;

import com.jabulani.ligiopen.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "favorited_clubs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoritedClub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "favorited_at")
    private LocalDateTime favoritedAt;

    @PrePersist
    protected void onCreate() {
        favoritedAt = LocalDateTime.now();
    }
}