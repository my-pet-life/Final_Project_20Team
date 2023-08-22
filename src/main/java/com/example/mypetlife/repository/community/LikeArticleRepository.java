package com.example.mypetlife.repository.community;

import com.example.mypetlife.entity.article.Article;
import com.example.mypetlife.entity.article.LikeArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeArticleRepository extends JpaRepository<LikeArticle, Long> {

    public Optional<LikeArticle> findByArticle(Article article);
}
