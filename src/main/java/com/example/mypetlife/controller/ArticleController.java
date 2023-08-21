package com.example.mypetlife.controller;

import com.example.mypetlife.dto.community.article.*;
import com.example.mypetlife.entity.User;
import com.example.mypetlife.entity.article.Article;
import com.example.mypetlife.entity.article.ArticleImage;
import com.example.mypetlife.entity.article.CategoryArticle;
import com.example.mypetlife.entity.article.Tag;
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
                                       @RequestPart List<CreateArticleTagDto> tagDtos,
                                       @RequestPart(required = false) List<MultipartFile> imageFiles) {

        // 글 작성한 회원 조회
        String email = jwtTokenUtils.getEmailFromHeader(request); // request에서 토큰을 받아서, 토큰에서 email을 받아옴
        User user = userService.findByEmail(email);

        // 태그 리스트 생성
        List<Tag> tags = new ArrayList<>();
        for (CreateArticleTagDto tagDto : tagDtos) {
            if(tagService.isExistInDb(tagDto.getTagName())) {
                // 이미 존재하는 태그이면 db에서 태그를 조회해와서 연결
                Tag findTag = tagService.findByTagName(tagDto.getTagName());
                tags.add(findTag);
            } else {
                // 존재하지 않은 태그이면 db에 생성
                tags.add(Tag.createTag(tagDto.getTagName()));
            }
        }

        // 게시글 이미지 리스트 생성
        List<ArticleImage> articleImages = new ArrayList<>();
        if(!imageFiles.isEmpty()) {
            for (MultipartFile imageFile : imageFiles) {
                articleImages.add(ArticleImage.createArticleImage(imageFile.getOriginalFilename()));
            }
        }

        // 게시글 생성
        Article article = Article.createArticle(dto.getTitle(), dto.getContent(),
                CategoryArticle.valueOf(dto.getCategory()),
                user, tags, articleImages);

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
//                                         @RequestPart List<CreateArticleTagDto> tagParam,
//                                         @RequestPart(required = false) List<MultipartFile> imageFiles) {
//
//        // 게시글 조회
//        Article article = articleService.findById(articleId);
//
//        // 태그 조회
//
//        return ArticleResponse.createResponse(article);
//    }
//
//    /**
//     * [DELETE] /community/article/{articleId}
//     * 게시글 삭제
//     */
//    @DeleteMapping("/community/article/{articleId}")
//    public MessageResponse DeleteArticle(@PathVariable Long articleId) {
//
//    }
}
