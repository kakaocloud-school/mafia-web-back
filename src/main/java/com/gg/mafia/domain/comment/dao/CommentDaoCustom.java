package com.gg.mafia.domain.comment.dao;

import com.gg.mafia.domain.comment.dto.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

public interface CommentDaoCustom {
    Page<CommentResponse> findComments(@NonNull Long profileId, @NonNull Pageable pageable);
}
