package com.example.mypetlife.init.community;

import com.example.mypetlife.entity.community.comment.Comment;
import com.example.mypetlife.entity.community.comment.LikeComment;
import com.example.mypetlife.entity.community.article.*;
import com.example.mypetlife.entity.user.Authority;
import com.example.mypetlife.entity.user.PetSpecies;
import com.example.mypetlife.entity.user.User;
import com.example.mypetlife.repository.UserRepository;
import com.example.mypetlife.repository.community.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Init {

    private final InitService initService;

    //@PostConstruct
    public void init() {
        initService.initUser();
        initService.initArticle();
        initService.initComment();
        initService.initLikeArticle();
        initService.initLikeComment();
    }

    @Service
    @Transactional
    @RequiredArgsConstructor
    @Slf4j
    static class InitService {

        private final UserRepository userRepository;
        private final ArticleRepository articleRepository;
        private final TagRepository tagRepository;
        private final CommentRepository commentRepository;
        private final LikeArticleRepository likeArticleRepository;
        private final LikeCommentRepository likeCommentRepository;

        private final PasswordEncoder passwordEncoder;

        public void initUser() {

            User user1 = User.createUser("kim", "kim@naver.com", passwordEncoder.encode("1111"),
                    "01012341234", "170201", PetSpecies.DOG, Authority.ROLE_USER);
            userRepository.save(user1);

            User user2 = User.createUser("lee", "lee@naver.com", passwordEncoder.encode("2222") ,
                    "01012345678", "201212", PetSpecies.CAT, Authority.ROLE_USER);
            userRepository.save(user2);

            User user3 = User.createUser("park", "park@naver.com", passwordEncoder.encode("3333"),
                    "01033332222", "220102", PetSpecies.ETC, Authority.ROLE_USER);
            userRepository.save(user3);

            User admin = User.createUser("admin", "admin@naver.com", passwordEncoder.encode("1111"),
                    "01033332222", "220102", PetSpecies.ETC, Authority.ROLE_ADMIN);
            userRepository.save(admin);
        }

        public void initArticle() {

            /*
             * 1. kim 게시글 작성
             */

            // User 조회
            User kim = userRepository.findByEmail("kim@naver.com").get();

            // Article 생성
            Article article = Article.createArticle("가입인사드립니다!", "잘부탁드려요 ㅎㅎ",
                                                    ArticleCategory.CHAT, kim);

            // Tag 생성
            Tag tag = Tag.createTag("가입인사");
            tagRepository.save(tag);

            // ArticleTag
            article.addArticleTag(ArticleTag.createArticleTag(tag));

            // ArticleImage
            ArticleImage articleImage = ArticleImage.createArticleImage("https://t1.daumcdn.net/cfile/tistory/99CBFB3C5AE2934205");
            article.addImage(articleImage);

            articleRepository.save(article);

            /*
             * 2. lee 게시글 작성
             */

            // User 조회
            User lee = userRepository.findByEmail("lee@naver.com").get();

            // Article 생성
            Article article2 = Article.createArticle("성남에 병원 추천해요!", "친절하셔서 좋았습니다",
                                                        ArticleCategory.REVIEW, lee);

            // Tag 생성
            Tag tag2 = Tag.createTag("정보공유");
            tagRepository.save(tag2);

            // ArticleTag
            article2.addArticleTag(ArticleTag.createArticleTag(tag2));

            // ArticleImage
            ArticleImage articleImage2 = ArticleImage.createArticleImage("https://t1.daumcdn.net/cfile/tistory/99CBFB3C5AE2934205");
            article.addImage(articleImage2);

            articleRepository.save(article2);

            /*
             * 3. park 게시글 작성
             */

            // User 조회
            User park = userRepository.findByEmail("park@naver.com").get();

            // Article 생성
            Article article3 = Article.createArticle("어떤 반려동물이 키우기 편한가요?", "추천해주세요",
                                                        ArticleCategory.QUESTION, park);

            // Tag 생성
            Tag tag3 = Tag.createTag("반려동물");
            Tag tag4 = Tag.createTag("추천");
            tagRepository.save(tag3);
            tagRepository.save(tag4);

            // ArticleTag
            article3.addArticleTag(ArticleTag.createArticleTag(tag3));
            article3.addArticleTag(ArticleTag.createArticleTag(tag4));

            articleRepository.save(article3);
        }

        public void initComment() {

            /*
             * park -> 게시글 1
             */
            // 회원 조회
            User park = userRepository.findByEmail("park@naver.com").get();

            // 게시글 조회
            Article article = articleRepository.findById(1L).get();

            // Comment 생성
            Comment comment = Comment.createComment("안녕하세요~", park, article);
            commentRepository.save(comment);

            /*
             * park -> 게시글 2
             */

            // 게시글 조회
            Article article2 = articleRepository.findById(2L).get();

            // Comment 생성
            Comment comment2 = Comment.createComment("정보 감사합니다~", park, article2);
            Comment comment3 = Comment.createComment("저도 가봐야겠어요!", park, article2);
            commentRepository.save(comment2);
            commentRepository.save(comment3);
        }

        public void initLikeArticle() {

            /*
             * kim -> 게시글 2
             */
            // 회원 조회
            User kim = userRepository.findByEmail("kim@naver.com").get();

            // 게시글 조회
            Article article = articleRepository.findById(2L).get();

            // 게시글 좋아요
            LikeArticle likeArticle = LikeArticle.createLikeArticle(kim, article);
            likeArticleRepository.save(likeArticle);

            /*
             * park -> 게시글 2
             */
            // 회원 조회
            User park = userRepository.findByEmail("park@naver.com").get();

            // 게시글 좋아요
            LikeArticle likeArticle2 = LikeArticle.createLikeArticle(park, article);
            likeArticleRepository.save(likeArticle2);
        }

        public void initLikeComment() {

            /*
             * kim이 자신의 게시글에 달린 댓글에 좋아요
             */
            // 회원 조회
            User kim = userRepository.findByEmail("kim@naver.com").get();

            // 댓글 조회
            Comment comment = commentRepository.findById(1L).get();

            // 댓글 좋아요
            LikeComment likeComment = LikeComment.createLikeComment(comment, kim);
            likeCommentRepository.save(likeComment);

            /*
             * kim이 lee의 게시글에 달린 댓글에 좋아요
             */
            // 댓글 조회
            Comment comment2 = commentRepository.findById(2L).get();

            // 댓글 좋아요
            LikeComment likeComment2 = LikeComment.createLikeComment(comment2, kim);
            likeCommentRepository.save(likeComment2);
        }
    }

}
