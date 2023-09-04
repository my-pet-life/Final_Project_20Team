package com.example.mypetlife.service.community;

import com.example.mypetlife.entity.community.article.*;
import com.example.mypetlife.entity.user.PetSpecies;
import com.example.mypetlife.exception.CustomException;
import com.example.mypetlife.exception.ErrorCode;
import com.example.mypetlife.repository.community.ArticleRepository;
import com.example.mypetlife.repository.community.ArticleTagRepository;
import com.example.mypetlife.repository.community.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleTagRepository articleTagRepository;
    private final TagRepository tagRepository;

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
    public void updateArticle(Article article, String title, String content, ArticleCategory category) {

        article.setTitle(title);
        article.setContent(content);
        article.setCategory(category);
    }

    public void updateArticleTags(Article article, List<ArticleTag> articleTags) {

        List<ArticleTag> oldArticleTags = article.getArticleTags();
        List<Tag> removeList = new ArrayList<>();

        // 기존 태그들 중 해당 게시글에만 있는 태그는 removeList에 담기
        for (ArticleTag articleTag : oldArticleTags) {
            if(articleTagRepository.findByTagName(articleTag.getTag().getTagName()).size() == 1) {
                removeList.add(articleTag.getTag());
            }
        }

        // 게시글에 기존 ArticleTag 제거
        oldArticleTags.clear();

        // 더이상 존재하지 않는 Tag DB에서 제거
        for (Tag tag : removeList) {
            tagRepository.delete(tag);
        }

        // 게시글에 새로운 ArticleTag 추가
        for (ArticleTag articleTag : articleTags) {
            article.addArticleTag(articleTag);
        }
    }

    @Transactional
    public void updateArticleImages(Article article, List<ArticleImage> articleImages) {

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
