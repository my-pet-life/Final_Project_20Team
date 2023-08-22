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

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final TagService tagService;
    private final EntityManager em;

    public Long saveArticle(Article article) {

        // 저장
        articleRepository.save(article);

        return article.getId();
    }

    public Article findById(Long id) {

        return articleRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ARTICLE));
    }

    public List<Article> findAll() {

        return articleRepository.findAll();
    }

    public List<Article> findByCategory(String categoryName) {

        return em.createQuery("select a from Article a where a.category = :category")
                .setParameter("category", ArticleCategory.valueOf(categoryName.toUpperCase()))
                .getResultList();
    }

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

    public void deleteArticle(Article article) {

        articleRepository.delete(article);
    }

    public List<Article> findByTagName(String tagName) {

        List<Article> articles = articleRepository.findAll();
        List<Article> result = new ArrayList<>();
        for (Article article : articles) {
            if(article.getArticleTags().stream().filter(articleTag -> articleTag.getTag().getTagName().equals(tagName)).findAny().isPresent()) {
                result.add(article);
            }
        }

        return result;
    }
}
