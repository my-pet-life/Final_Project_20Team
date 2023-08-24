package com.example.mypetlife.service.community;

import com.example.mypetlife.entity.article.Article;
import com.example.mypetlife.entity.article.LikeArticle;
import com.example.mypetlife.entity.user.User;
import com.example.mypetlife.repository.community.LikeArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeArticleService {

    private final LikeArticleRepository likeArticleRepository;
    private final ArticleService articleService;

    /*
     * 저장
     */
    public void saveLike(User user, Long articleId) {

        likeArticleRepository.findByArticleIdAndUsername(articleId, user.getUsername())
                .ifPresentOrElse(
                        // 조회 결과가 있으면(이미 좋아요를 누른 상태) 취소
                        likeArticle -> likeArticleRepository.delete(likeArticle),
                        // 조회 결과가 없으면 좋아요 생성 및 저장
                        () -> {
                            Article article = articleService.findById(articleId);
                            likeArticleRepository.save(LikeArticle.createLikeArticle(user, article));}
                );
    }
}
