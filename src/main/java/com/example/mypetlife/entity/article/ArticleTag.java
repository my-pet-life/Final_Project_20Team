package com.example.mypetlife.entity.article;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleTag {

    @Id @GeneratedValue
    @Column(name = "article_tag_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "article_id")
    Article article;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    Tag tag;

    //==생성 메서드==//
    public static ArticleTag createArticleTag(Tag tag) {

        ArticleTag articleTag = new ArticleTag();
        articleTag.tag = tag;
        return articleTag;
    }

    //==수정 메서드==//

    public void setArticle(Article article) {
        this.article = article;
    }
}
