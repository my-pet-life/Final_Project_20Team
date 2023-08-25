package com.example.mypetlife.service.community;

import com.example.mypetlife.entity.community.comment.Comment;
import com.example.mypetlife.entity.community.comment.LikeComment;
import com.example.mypetlife.entity.user.User;
import com.example.mypetlife.repository.community.LikeCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeCommentService {

    private final LikeCommentRepository likeCommentRepository;
    private final CommentService commentService;

    public void likeComment(User user, Long commentId) {

        Optional<LikeComment> optionalLikeComment = likeCommentRepository.findByCommentIdAndUsername(commentId, user.getUsername());
        if(optionalLikeComment.isPresent()) {
            // 이미 좋아요를 누른 상태이면 좋아요 취소
            likeCommentRepository.delete(optionalLikeComment.get());
        } else {
            // 좋아요 누르기
            Comment comment = commentService.findById(commentId);
            LikeComment likeComment = LikeComment.createLikeComment(comment, user);
            likeCommentRepository.save(likeComment);

        }
    }
}
