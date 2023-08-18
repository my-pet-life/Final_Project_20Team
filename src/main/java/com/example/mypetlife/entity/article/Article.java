package com.example.mypetlife.entity.article;

import com.example.mypetlife.entity.Comment;
import com.example.mypetlife.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "article")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    @Enumerated(value = EnumType.STRING)
    private CategoryArticle category;

    @Column(name = "post_date")
    private LocalDateTime postDate;
    private Integer likes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<ArticleImage> images = new ArrayList<>();

    //==연관관계 편의 메서드==//
    public void addTag(Tag tag) {

        tag.setArticle(this);
        this.tags.add(tag);
    }

    public void addImage(ArticleImage articleImage) {

        articleImage.setArticle(this);
        this.images.add(articleImage);
    }

    public void addComment(Comment comment) {

        comment.setArticle(this);
        this.comments.add(comment);
    }

    public void setUser(User user) {

        user.getArticles().add(this);
        this.user = user;
    }

    //==생성 메서드==//
    public static Article createArticle(String title, String content, CategoryArticle category,
                                        User user, List<Tag> tags, List<ArticleImage> articleImages) {

        Article article = new Article();
        article.title = title;
        article.content = content;
        article.category = category;
        article.postDate = LocalDateTime.now();
        article.likes = 0;
        article.setUser(user);
        for (Tag tag : tags) {
            article.addTag(tag);
        }
        for (ArticleImage articleImage : articleImages) {
            article.addImage(articleImage);
        }

        return article;
    }
}
