package com.example.mypetlife.entity.article;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tag")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag_name")
    private String tagName;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    //==수정 메서드==//
    public void setArticle(Article article) {
        this.article = article;
    }

    //==생성 메서드==//
    public static Tag createTag(String tagName) {

        Tag tag = new Tag();
        tag.tagName = tagName;
        return tag;
    }
}