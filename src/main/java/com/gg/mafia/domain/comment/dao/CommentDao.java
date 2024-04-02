package com.gg.mafia.domain.comment.dao;

import com.gg.mafia.domain.comment.domain.Comment;
import com.gg.mafia.domain.comment.dto.CommentResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface CommentDao extends JpaRepository<Comment, Long>, CommentDaoCustom {
    List<Comment> findByUserId(Long id);

    List<Comment> findByProfileId(Long id);

    List<Comment> findByUserIdAndProfileId(Long id, Long id1);

    Optional<Comment> findByUserIdAndId(Long userId, Long commentId);

    @Override
    Page<CommentResponse> findComments(@NonNull Long profileId, @NonNull Pageable pageable);
}
