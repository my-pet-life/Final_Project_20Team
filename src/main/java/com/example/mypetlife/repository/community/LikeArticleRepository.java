package com.example.mypetlife.repository.community;

import com.example.mypetlife.entity.article.Article;
import com.example.mypetlife.entity.article.LikeArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeArticleRepository extends JpaRepository<LikeArticle, Long> {

    Optional<LikeArticle> findByArticle(Article article);

    @Query("select la from LikeArticle la where la.article.id = :articleId and la.user.username = :username")
    Optional<LikeArticle> findByArticleIdAndUsername(@Param("articleId") Long articleId, @Param("username") String username);
}
