package com.gg.mafia.domain.comment.dto;

import com.gg.mafia.domain.comment.domain.Comment;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentResponse {
    private String userName;
    private Comment comment;

    @QueryProjection
    public CommentResponse(
            String userName,
            Comment comment
    ) {
        this.userName = userName;
        this.comment = comment;
    }
}
