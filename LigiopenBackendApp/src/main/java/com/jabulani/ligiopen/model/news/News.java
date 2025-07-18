package com.jabulani.ligiopen.model.news;

import com.jabulani.ligiopen.model.aws.File;
import com.jabulani.ligiopen.model.club.entity.Club;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "coverPhotoId")
    private File coverPhoto;

    private String title;

    private String subTitle;

    private Boolean neutral;
    private LocalDateTime publishedAt;

    @Enumerated(EnumType.STRING)
    private NewsStatus newsStatus;

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NewsItem> newsItems;

    @ManyToMany(mappedBy = "news", fetch = FetchType.LAZY)
    private List<Club> clubs;
}
