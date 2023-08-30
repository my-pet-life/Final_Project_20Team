package com.example.mypetlife.repository.community;

import com.example.mypetlife.entity.community.article.Article;
import com.example.mypetlife.entity.community.article.ArticleCategory;
import com.example.mypetlife.entity.user.PetSpecies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    /* 전체 조회, 종 필터 */
    @Query("select a from Article a join a.user u where u.petSpecies = :petSpecies")
    Page<Article> findAllByPetSpecies(@Param("petSpecies") PetSpecies petSpecies, Pageable pageable);

    /* 게시판별 조회 */
    Page<Article> findByCategory(ArticleCategory category, Pageable pageable);

    /* 게시판별 조회, 종 필터 */
    @Query("select a from Article a join a.user u where u.petSpecies = :petSpecies and a.category = :articleCategory")
    Page<Article> findByCategoryAndPetSpecies(@Param("articleCategory") ArticleCategory articleCategory,
                                              @Param("petSpecies") PetSpecies petSpecies,
                                              Pageable pageable);

    /* 태그별 조회 */
    @Query("select a from Article a join a.articleTags at where at.tag.tagName = :tagName")
    Page<Article> findByTagName(@Param("tagName") String tagName, Pageable pageable);

    /* 태그별 조회, 종 필터 */
    @Query("select a from Article a join a.articleTags at join a.user u where at.tag.tagName = :tagName and u.petSpecies = :petSpecies")
    Page<Article> findByTagNameAndPetSpecies(@Param("tagName") String tagName,
                                             @Param("petSpecies") PetSpecies petSpecies,
                                             Pageable pageable);
}
