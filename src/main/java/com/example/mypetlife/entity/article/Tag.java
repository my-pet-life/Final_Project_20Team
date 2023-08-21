package com.example.mypetlife.entity.article;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "tag")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

    @Id @GeneratedValue
    @Column(name = "tag_id")
    private Long id;

    @Column(name = "tag_name")
    private String tagName;

    @OneToMany(mappedBy = "tag")
    private List<ArticleTag> articleTags;

    //==생성 메서드==//
    public static Tag createTag(String tagName) {

        Tag tag = new Tag();
        tag.tagName = tagName;
        return tag;
    }
}
