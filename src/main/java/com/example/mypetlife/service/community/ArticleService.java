package com.example.mypetlife.service.community;

import com.example.mypetlife.entity.article.Article;
import com.example.mypetlife.entity.article.CategoryArticle;
import com.example.mypetlife.exception.CustomException;
import com.example.mypetlife.exception.ErrorCode;
import com.example.mypetlife.repository.ArticleRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ArticleService {

    private final ArticleRepository articleRepository;
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
                .setParameter("category", CategoryArticle.valueOf(categoryName.toUpperCase()))
                .getResultList();
    }
}
