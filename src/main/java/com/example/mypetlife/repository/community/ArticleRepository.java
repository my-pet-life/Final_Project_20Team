package com.example.mypetlife.repository.community;

import com.example.mypetlife.entity.community.article.Article;
import com.example.mypetlife.entity.community.article.ArticleCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    // 카테고리별 조회
    Page<Article> findByCategory(ArticleCategory category, Pageable pageable);

    // 태그로 조회
    // (페이징에 대한 쿼리는 작성하지 않아도 JPA가 인자로 들어오는 Pageable을 보고 알아서 페이징 처리를 해줌)
    @Query("select a from Article a join a.articleTags at where at.tag.tagName = :tagName")
    Page<Article> findByTagName(@Param("tagName") String tagName, Pageable pageable);
}
