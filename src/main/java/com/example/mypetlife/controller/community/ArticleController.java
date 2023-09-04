package com.example.mypetlife.controller.community;

import com.example.mypetlife.dto.MessageResponse;
import com.example.mypetlife.dto.community.article.*;
import com.example.mypetlife.entity.community.article.*;
import com.example.mypetlife.entity.user.PetSpecies;
import com.example.mypetlife.entity.user.User;
import com.example.mypetlife.exception.CustomException;
import com.example.mypetlife.exception.ErrorCode;
import com.example.mypetlife.jwt.JwtTokenUtils;
import com.example.mypetlife.service.community.ArticleService;
import com.example.mypetlife.service.UserService;
import com.example.mypetlife.service.community.LikeArticleService;
import com.example.mypetlife.service.community.TagService;
import io.swagger.v3.oas.annotations.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@io.swagger.v3.oas.annotations.tags.Tag(name = "게시글", description = "게시글 관련 api")
public class ArticleController {

    private final ArticleService articleService;
    private final LikeArticleService likeArticleService;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserService userService;
    private final TagService tagService;

    /**
     * [POST] /community
     * 게시글 등록
     */
    @PostMapping("/community")
    @Operation(summary = "게시글 등록")
    public ArticleResponse postArticle(HttpServletRequest request,
                                       @RequestPart @Validated CreateArticleRequest dto,
                                       @RequestPart(required = false) List<CreateArticleTagRequest> tagDtos,
                                       @RequestPart(required = false) List<MultipartFile> imageFiles) {

        // User 조회
        String email = jwtTokenUtils.getEmailFromHeader(request);
        User user = userService.findByEmail(email);

        // Article 생성
        Article article = Article.createArticle(dto.getTitle(), dto.getContent(),
                ArticleCategory.valueOf(dto.getCategory().toUpperCase()), user);

        // 태그 연관관계
        if(tagDtos != null) {
            for (CreateArticleTagRequest tagDto : tagDtos) {
                if(tagService.isNewTag(tagDto.getTagName())) {
                    // 새로운 태그인  경우
                    // Tag 생성 및 저장
                    Tag newTag = Tag.createTag(tagDto.getTagName());
                    tagService.saveTag(newTag);
                    // ArticleTag 생성
                    ArticleTag articleTag = ArticleTag.createArticleTag(newTag);
                    // 연관관계
                    article.addArticleTag(articleTag);

                } else {
                    // 이미 존재하는 태그인 경우, DB에서 태그를 조회해와서 연결
                    Tag findTag = tagService.findByTagName(tagDto.getTagName());
                    // ArticleTag 생성
                    ArticleTag articleTag = ArticleTag.createArticleTag(findTag);
                    // 연관관계
                    article.addArticleTag(articleTag);
                }
            }
        }

        // 이미지 연관관계
        if(imageFiles != null) {
            for (MultipartFile imageFile : imageFiles) {
                article.addImage(ArticleImage.createArticleImage(imageFile.getOriginalFilename()));
            }
        }

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
    @Operation(summary = "전체 게시글 조회")
    public Page<ArticleListResponse> readArticles(@PageableDefault(size = 5, sort = "createdDate",
                                                    direction = Sort.Direction.DESC) Pageable pageable,
                                                  @RequestParam(name = "species", required = false) String species) {

        Page<Article> articlePage = null;
        if(species != null) {
            PetSpecies petSpecies = PetSpecies.valueOf(species.toUpperCase());
            articlePage = articleService.findAll(petSpecies, pageable);
        } else {
            articlePage = articleService.findAll(pageable);
        }

        return articlePage.map(article -> ArticleListResponse.createResponse(article));
    }

    /**
     * [GET] /community/articles/{categoryName}
     * 게시판별 조회
     */
    @GetMapping("/community/articles/{categoryName}")
    @Operation(summary = "게시판별 조회")
    public Page<ArticleListResponse> readArticlesByCategory(@PathVariable String categoryName,
                                                            @PageableDefault(size = 5, sort = "createdDate",
                                                                    direction = Sort.Direction.DESC) Pageable pageable,
                                                            @RequestParam(name = "species", required = false) String species) {

        ArticleCategory articleCategory = ArticleCategory.valueOf(categoryName.toUpperCase());

        Page<Article> articlePage = null;
        if(species != null) {
            PetSpecies petSpecies = PetSpecies.valueOf(species.toUpperCase());
            articlePage = articleService.findByCategory(articleCategory, petSpecies, pageable);
        } else {
            articlePage = articleService.findByCategory(articleCategory, pageable);
        }

        return articlePage.map(article -> ArticleListResponse.createResponse(article));
    }

    /**
     * [GET] /community/search/tag/{tagName}
     * 태그별 게시글 조회
     */
    @GetMapping("/community/search/tag/{tagName}")
    @Operation(summary = "태그별 조회")
    public Page<ArticleListResponse> readArticlesByTagName(@PathVariable String tagName,
                                                           @PageableDefault(size = 5, sort = "createdDate",
                                                                   direction = Sort.Direction.DESC) Pageable pageable,
                                                           @RequestParam(name = "species", required = false) String species) {

        Page<Article> articlePage = null;
        if(species != null) {
            PetSpecies petSpecies = PetSpecies.valueOf(species.toUpperCase());
            articlePage = articleService.findByTagName(tagName, petSpecies, pageable);
        } else {
            articlePage = articleService.findByTagName(tagName, pageable);
        }
        return articlePage.map(article -> ArticleListResponse.createResponse(article));
    }

    /**
     * [GET] /community/article/{articleId}
     * 게시글 단일 조회
     */
    @GetMapping("/community/article/{articleId}")
    @Operation(summary = "단일 게시글 조회")
    public ArticleResponse readArticle(@PathVariable Long articleId) {

        Article article = articleService.findById(articleId);
        return ArticleResponse.createResponse(article);
    }

    /**
     * [PUT] /community/article/{articleId}
     * 게시글 수정
     */
    @PutMapping("/community/article/{articleId}")
    @Operation(summary = "게시글 수정")
    public ArticleResponse updateArticle(@PathVariable Long articleId,
                                         @RequestPart @Validated UpdateArticleRequest dto,
                                         @RequestPart(required = false) List<CreateArticleTagRequest> tagDtos,
                                         @RequestPart(required = false) List<MultipartFile> imageFiles,
                                         HttpServletRequest request) {

        // 회원 검증
        Article article = articleService.findById(articleId);
        User user = article.getUser();
        validateUser(request, user);

        // 제목, 글내용, 카테고리 수정
        articleService.updateArticle(article, dto.getTitle(), dto.getContent(), ArticleCategory.valueOf(dto.getCategory().toUpperCase()));

        // 태그 수정
        List<ArticleTag> articleTags = new ArrayList<>();
        if(tagDtos != null) {
            for (CreateArticleTagRequest tagDto : tagDtos) {
                if(tagService.isNewTag(tagDto.getTagName())) {
                    // 새로운 태그 -> 태그 생성 및 저장
                    Tag newTag = Tag.createTag(tagDto.getTagName());
                    tagService.saveTag(newTag);
                    // ArticleTag 생성
                    ArticleTag articleTag = ArticleTag.createArticleTag(newTag);
                    articleTags.add(articleTag);
                } else {
                    // 이미 존재하는 태그 -> DB에서 태그를 조회해와서 연결
                    Tag findTag = tagService.findByTagName(tagDto.getTagName());
                    // ArticleTag 생성
                    ArticleTag articleTag = ArticleTag.createArticleTag(findTag);
                    articleTags.add(articleTag);
                }
            }
        }

        articleService.updateArticleTags(article, articleTags);

        // 이미지 수정
        List<ArticleImage> articleImages = new ArrayList<>();
        if(imageFiles != null) {
            for (MultipartFile imageFile : imageFiles) {
                articleImages.add(ArticleImage.createArticleImage(imageFile.getOriginalFilename()));
            }
        }
        articleService.updateArticleImages(article, articleImages);

        // 응답 생성
        Article savedArticle = articleService.findById(articleId);
        ArticleResponse articleResponse = ArticleResponse.createResponse(savedArticle);
        return articleResponse;
    }

    /**
     * [DELETE] /community/article/{articleId}
     * 게시글 삭제
     */
    @DeleteMapping("/community/article/{articleId}")
    @Operation(summary = "게시글 삭제")
    public MessageResponse DeleteArticle(@PathVariable Long articleId, HttpServletRequest request) {

        // 회원 검증
        Article article = articleService.findById(articleId);
        User user = article.getUser();
        validateUser(request, user);

        // 삭제
        articleService.deleteArticle(article);

        MessageResponse response = new MessageResponse("게시글이 삭제되었습니다");
        return response;
    }

    /**
     * [POST] /community/article/{articleId}/like
     * 게시글 좋아요 누르기
     */
    @PostMapping("/community/article/{articleId}/like")
    @Operation(summary = "게시글 좋아요 등록/취소")
    public ArticleResponse likeArticle(HttpServletRequest request, @PathVariable Long articleId) {

        // 회원 조회
        String email = jwtTokenUtils.getEmailFromHeader(request);
        User user = userService.findByEmail(email);

        // 좋아요 저장/취소
        likeArticleService.saveLike(user, articleId);

        Article article = articleService.findById(articleId);
        return ArticleResponse.createResponse(article);
    }


    /*
     * 로그인 회원 검증
     */
    private void validateUser(HttpServletRequest request, User user) {

        String email = jwtTokenUtils.getEmailFromHeader(request);
        User loginUser = userService.findByEmail(email);
        if(!loginUser.equals(user)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }
}
