package com.example.mypetlife.service.community;

import com.example.mypetlife.entity.Comment;
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
}
