package com.jabulani.ligiopen.model.news;

import com.jabulani.ligiopen.model.aws.File;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "newsItem")
public class NewsItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String subTitle;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String paragraph;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "fileId")
    private File file;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "newsId")
    private News news;
}
