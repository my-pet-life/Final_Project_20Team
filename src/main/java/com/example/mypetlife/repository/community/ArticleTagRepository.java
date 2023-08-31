package com.example.mypetlife.repository.community;

import com.example.mypetlife.entity.community.article.Article;
import com.example.mypetlife.entity.community.article.ArticleTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleTagRepository extends JpaRepository<ArticleTag, Long> {

    @Query("select at from ArticleTag at join at.tag t where t.tagName = :tagName")
    List<ArticleTag> findByTagName(@Param("tagName") String tagName);
}
