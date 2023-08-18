package com.example.mypetlife.entity.article;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "article_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    //==수정 메서드==//
    public void setArticle(Article article) {
        this.article = article;
    }

    //==생성 메서드==//
    public static ArticleImage createArticleImage(String imageUrl) {

        ArticleImage articleImage = new ArticleImage();
        articleImage.imageUrl = imageUrl;
        return articleImage;
    }
}
