package com.example.mypetlife.controller;

import com.example.mypetlife.dto.community.article.*;
import com.example.mypetlife.entity.User;
import com.example.mypetlife.entity.article.*;
import com.example.mypetlife.jwt.JwtTokenUtils;
import com.example.mypetlife.service.community.ArticleService;
import com.example.mypetlife.service.UserService;
import com.example.mypetlife.service.community.TagService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ArticleController {

    private final ArticleService articleService;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserService userService;
    private final TagService tagService;

    /**
     * [POST] /community
     * 게시글 등록
     */
    @PostMapping("/community")
    public ArticleResponse postArticle(HttpServletRequest request,
                                       @RequestPart @Validated CreateArticleRequest dto,
                                       @RequestPart(required = false) List<CreateArticleTagDto> tagDtos,
                                       @RequestPart(required = false) List<MultipartFile> imageFiles) {

        // User 조회
        String email = jwtTokenUtils.getEmailFromHeader(request);
        User user = userService.findByEmail(email);

        // List<ArticleTag>
        List<ArticleTag> articleTags = new ArrayList<>();
        if(tagDtos != null) {
            for (CreateArticleTagDto tagDto : tagDtos) {
                if(tagService.isExistInDb(tagDto.getTagName())) {
                    // 이미 존재하는 태그이면 DB에서 태그를 조회해와서 연결
                    // Tag 조회
                    Tag findTag = tagService.findByTagName(tagDto.getTagName());
                    // ArticleTag 생성
                    ArticleTag articleTag = ArticleTag.createArticleTag(findTag);
                    articleTags.add(articleTag);
                } else {
                    // 존재하지 않은 태그이면 DB에 생성
                    // Tag 생성
                    Tag newTag = Tag.createTag(tagDto.getTagName());
                    // ArticleTag 생성
                    ArticleTag articleTag = ArticleTag.createArticleTag(newTag);
                    articleTags.add(articleTag);
                }
            }
        }

        // List<ArticleImage>
        List<ArticleImage> articleImages = new ArrayList<>();
        if(imageFiles != null) {
            for (MultipartFile imageFile : imageFiles) {
                articleImages.add(ArticleImage.createArticleImage(imageFile.getOriginalFilename()));
            }
        }

        // Article 생성
        Article article = Article.createArticle(dto.getTitle(), dto.getContent(),
                CategoryArticle.valueOf(dto.getCategory()),
                user, articleTags, articleImages);

        // 게시글 저장
        Long id = articleService.saveArticle(article);

        // 응답 생성
        Article savedArticle = articleService.findById(id);
        ArticleResponse articleResponse = ArticleResponse.createResponse(savedArticle);
        return articleResponse;
    }

    /**
     * [GET] /community/articles
     * 전체 게시글 조회
     */
    @GetMapping("/community/articles")
    public ArticlesResponse readArticles() {

        List<Article> articles = articleService.findAll();
        ArticlesResponse response = ArticlesResponse.createResponse(articles);
        return response;
    }

    /**
     * [GET] /community/articles/{categoryName}
     * 게시판 별 게시글 조회
     */
    @GetMapping("/community/articles/{categoryName}")
    public ArticlesResponse readArticlesByCategory(@PathVariable String categoryName) {

        List<Article> articles = articleService.findByCategory(categoryName);
        ArticlesResponse response = ArticlesResponse.createResponse(articles);
        return response;
    }

    /**
     * [GET] /community/article/{articleId}
     * 게시글 단일 조회
     */
    @GetMapping("/community/article/{articleId}")
    public ArticleResponse readArticle(@PathVariable Long articleId) {

        Article article = articleService.findById(articleId);
        return ArticleResponse.createResponse(article);
    }

//    /**
//     * [PUT] /community/article/{articleId}
//     * 게시글 수정
//     */
//    @PutMapping("/community/article/{articleId}")
//    public ArticleResponse updateArticle(@PathVariable Long articleId,
//                                         @RequestPart @Validated UpdateArticleRequest dto,
//                                         @RequestPart List<CreateArticleTagDto> tagDtos,
//                                         @RequestPart(required = false) List<MultipartFile> imageFiles,
//                                         HttpServelet) {
//
//    }

//    /**
//     * [DELETE] /community/article/{articleId}
//     * 게시글 삭제
//     */
//    @DeleteMapping("/community/article/{articleId}")
//    public MessageResponse DeleteArticle(@PathVariable Long articleId) {
//
//    }
}
