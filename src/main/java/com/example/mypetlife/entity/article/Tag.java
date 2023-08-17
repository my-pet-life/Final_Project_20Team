package com.example.mypetlife.entity.article;

import jakarta.persistence.*;

@Entity
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tagName;

    // article_id
    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;
}
