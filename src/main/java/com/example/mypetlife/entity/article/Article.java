package com.example.mypetlife.entity.article;

import com.example.mypetlife.entity.Comment;
import com.example.mypetlife.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private CATEGORY_ARTICLE type;

    @Column(name = "post_date")
    private LocalDateTime postDate;
    private Integer likes;

    // user_id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // comments
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Comment> comments;

    // tag
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Tag> tags;

    // article_images
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<ArticleImages> images;
}
