package com.gg.mafia.domain.comment.dao;

import com.gg.mafia.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentDao extends JpaRepository<Comment, Long>, CommentDaoCustom {
    Comment findByUserId(Long id);

    Comment findByProfileId(Long id);

    Comment findByUserIdAndProfileId(Long id, Long id1);
}
