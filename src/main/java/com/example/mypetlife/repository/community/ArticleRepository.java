package com.example.mypetlife.repository.community;

import com.example.mypetlife.entity.article.Article;
import com.example.mypetlife.entity.article.ArticleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("select a from Article a where a.category = :category")
    List<Article> findByCategory(@Param("category") ArticleCategory category);

    @Query("select a from Article a join a.articleTags at where at.tag.tagName = :tagName")
    List<Article> findByTagName(@Param("tagName") String tagName);
}
