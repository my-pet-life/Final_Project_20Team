package com.example.mypetlife.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String imageUrl;
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
