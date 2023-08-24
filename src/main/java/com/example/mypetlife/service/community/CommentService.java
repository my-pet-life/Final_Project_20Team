package com.example.mypetlife.service.community;

import com.example.mypetlife.entity.Comment;
import com.example.mypetlife.exception.CustomException;
import com.example.mypetlife.exception.ErrorCode;
import com.example.mypetlife.repository.community.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public void saveComment(Comment comment) {

        commentRepository.save(comment);
    }

    public Comment findById(Long commentId) {

        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));
    }

    public void updateComment(Comment comment, String content) {

        comment.updateContent(content);
    }

    public void deleteComment(Comment comment) {

        commentRepository.delete(comment);
    }
}
