package com.example.mypetlife.entity.article;

import jakarta.persistence.*;

@Entity
@Table(name = "article_images")
public class ArticleImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageUrl;

    // article_id
    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;
}
