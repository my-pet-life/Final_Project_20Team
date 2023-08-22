package com.example.mypetlife.service.community;

import com.example.mypetlife.entity.article.*;
import com.example.mypetlife.exception.CustomException;
import com.example.mypetlife.exception.ErrorCode;
import com.example.mypetlife.repository.community.ArticleRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final TagService tagService;
    private final EntityManager em;

    /*
     * 저장
     */
    @Transactional
    public Long saveArticle(Article article) {

        articleRepository.save(article);
        return article.getId();
    }

    /*
     * 단건 조회
     */
    public Article findById(Long id) {

        return articleRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ARTICLE));
    }

    /*
     * 전체 조회
     */
    public List<Article> findAll() {

        return articleRepository.findAll();
    }

    /*
     * 카테고리별 조회
     */
    public List<Article> findByCategory(String categoryName) {

        ArticleCategory articleCategory = ArticleCategory.valueOf(categoryName.toUpperCase());
        return articleRepository.findByCategory(articleCategory);
    }

    /*
     * 태그별 조회
     */
    public List<Article> findByTagName(String tagName) {

        return articleRepository.findByTagName(tagName);
    }

    /*
     * 수정
     */
    @Transactional
    public Long updateArticle(Article article, String title, String content,
                              List<ArticleTag> articleTags, List<ArticleImage> articleImages) {

        article.updateTitle(title);
        article.updateContent(content);

        // 기존 관계 제거
        List<ArticleTag> deleteArticleTags = article.getArticleTags();
        for (ArticleTag deleteArticleTag : deleteArticleTags) {
            em.remove(deleteArticleTag);
        }
        // 새로운 관계 추가
        article.updateArticleTags(articleTags);

        article.setImages(articleImages);

        return article.getId();
    }

    /*
     * 삭제
     */
    @Transactional
    public void deleteArticle(Article article) {

        articleRepository.delete(article);
    }
}
