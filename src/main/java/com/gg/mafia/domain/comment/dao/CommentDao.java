package com.gg.mafia.domain.comment.dao;

import com.gg.mafia.domain.comment.domain.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentDao extends JpaRepository<Comment, Long>, CommentDaoCustom {
    List<Comment> findByUserId(Long id);

    List<Comment> findByProfileId(Long id);

    List<Comment> findByUserIdAndProfileId(Long id, Long id1);

    Optional<Comment> findByUserIdAndId(Long userId, Long commentId);
}
