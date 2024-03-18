package com.gg.mafia.domain.comment.dao;
import com.gg.mafia.domain.comment.domain.Comment;
import com.gg.mafia.domain.member.domain.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface CommentDao extends JpaRepository<Comment, Long> {

    @Override
    @NonNull
    Page<Comment> findAll(@NonNull Pageable pageable);

    @Override
    @NonNull
    Optional<Comment> findById(@NonNull Long id);


    @NonNull
    Optional<Role> findCommentsBy(@NonNull String comment);
}
