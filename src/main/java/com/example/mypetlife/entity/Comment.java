package com.example.mypetlife.entity;

import com.example.mypetlife.entity.article.Article;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "comment_date")
    private LocalDateTime commentDate;

    // user_id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // article_id
    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;
}
