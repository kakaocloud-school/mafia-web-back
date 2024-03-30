package com.gg.mafia.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequest {
    @NotBlank
    Long commentId;
    @NotBlank
    Long profileId;
    @NotBlank
    String content;
    @NotBlank
    String updateContent;

}
