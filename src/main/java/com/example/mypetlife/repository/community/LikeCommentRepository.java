package com.example.mypetlife.repository.community;

import com.example.mypetlife.entity.community.comment.LikeComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {

    @Query("select lc from LikeComment lc where lc.comment.id = :commentId and lc.user.username = :username")
    Optional<LikeComment> findByCommentIdAndUsername(@Param("commentId") Long commentId, @Param("username") String username);
}
