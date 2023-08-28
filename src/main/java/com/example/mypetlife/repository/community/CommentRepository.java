package com.example.mypetlife.repository.community;

import com.example.mypetlife.entity.community.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
