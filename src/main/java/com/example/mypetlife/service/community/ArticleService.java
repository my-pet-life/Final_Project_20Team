package com.example.mypetlife.service.community;

import com.example.mypetlife.entity.community.article.Article;
import com.example.mypetlife.entity.community.article.ArticleCategory;
import com.example.mypetlife.entity.community.article.ArticleImage;
import com.example.mypetlife.entity.community.article.ArticleTag;
import com.example.mypetlife.entity.user.PetSpecies;
import com.example.mypetlife.exception.CustomException;
import com.example.mypetlife.exception.ErrorCode;
import com.example.mypetlife.repository.community.ArticleRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final EntityManager em;

    /* 저장 */
    @Transactional
    public Long saveArticle(Article article) {

        articleRepository.save(article);
        return article.getId();
    }

    /* 단건 조회 */
    public Article findById(Long id) {

        return articleRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ARTICLE));
    }

    /* 전체 조회 */
    public Page<Article> findAll(Pageable pageable) {

        return articleRepository.findAll(pageable);
    }

    /* 전체 조회, 종 필터 */
    public Page<Article> findAll(PetSpecies petSpecies, Pageable pageable) {

        return articleRepository.findAllByPetSpecies(petSpecies, pageable);
    }

    /* 게시판별 조회 */
    public Page<Article> findByCategory(ArticleCategory articleCategory, Pageable pageable) {

        return articleRepository.findByCategory(articleCategory, pageable);
    }

    /* 게시판별 조회, 종 필터 */
    public Page<Article> findByCategory(ArticleCategory articleCategory, PetSpecies petSpecies, Pageable pageable) {

        return articleRepository.findByCategoryAndPetSpecies(articleCategory, petSpecies, pageable);
    }

    /* 태그별 조회 */
    public Page<Article> findByTagName(String tagName, Pageable pageable) {

        return articleRepository.findByTagName(tagName, pageable);
    }

    /* 태그별 조회, 종 필터 */
    public Page<Article> findByTagName(String tagName, PetSpecies petSpecies, Pageable pageable) {

        return articleRepository.findByTagNameAndPetSpecies(tagName, petSpecies, pageable);
    }

    /* 수정 */
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

        article.setArticleImages(articleImages);

        return article.getId();
    }

    @Transactional
    public void updateArticle(Article article, String title, String content, ArticleCategory articleCategory) {

        article.updateTitle(title);
        article.updateContent(content);
        article.updateCategory(articleCategory);
    }

    public void updateArticleTags(Article article, List<ArticleTag> articleTags) {

//        // 기존 관계 제거
//        List<ArticleTag> deleteArticleTags = article.getArticleTags();
//        for (ArticleTag deleteArticleTag : deleteArticleTags) {
//            em.remove(deleteArticleTag);
//        }
//        // 새로운 관계 추가
//        article.updateArticleTags(articleTags);

        article.getArticleTags().clear();
        for (ArticleTag articleTag : articleTags) {
            article.addArticleTag(articleTag);
        }
    }

    @Transactional
    public void updateArticleImages(Article article, List<ArticleImage> articleImages) {

//        // 새롭게 참조하도록 (참조가 끊긴 ArticleImage는 DB에서 자동 삭제됨)
//        article.setArticleImages(articleImages);
        article.getArticleImages().clear();

        for (ArticleImage articleImage : articleImages) {
            article.addImage(articleImage);
        }
    }

    /* 삭제 */
    @Transactional
    public void deleteArticle(Article article) {

        articleRepository.delete(article);
    }


}
