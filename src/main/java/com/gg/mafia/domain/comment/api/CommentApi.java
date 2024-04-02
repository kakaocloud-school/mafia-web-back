package com.gg.mafia.domain.comment.api;

import com.gg.mafia.domain.comment.application.CommentService;
import com.gg.mafia.domain.comment.dto.CommentRequest;
import com.gg.mafia.domain.comment.dto.CommentResponse;
import com.gg.mafia.global.common.response.ApiResponse;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentApi {
    private final CommentService commentService;

    @GetMapping("/profiles/{profileId}")
    public ResponseEntity<ApiResponse<Page<CommentResponse>>> showCommentsByProfile(Principal principal,
                                                                                    @PathVariable Long profileId,
                                                                                    @PageableDefault Pageable pageable) {
        Page<CommentResponse> comments = commentService.selectComments(profileId, pageable);
        return ResponseEntity.ok(ApiResponse.success(comments));
    }

    @PostMapping("/profiles/{profileId}")
    public ResponseEntity<ApiResponse<Void>> saveComment(Principal principal, @PathVariable Long profileId,
                                                         @RequestBody CommentRequest request) {
        String email = principal.getName();
        commentService.insertComment(email, profileId,
                request.getContent());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{commentId}/profiles/{profileId}")
    public ResponseEntity<ApiResponse<Void>> updateComment(Principal principal, @PathVariable Long profileId,
                                                           @RequestBody CommentRequest request) {
        String email = principal.getName();
        commentService.updateComment(email, request.getCommentId(), request.getUpdateContent());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}/profiles/{profileId}")
    public ResponseEntity<ApiResponse<Void>> removeComment(Principal principal, @RequestBody CommentRequest request) {
        commentService.deleteComment(request.getCommentId());
        return ResponseEntity.ok().build();

    }
}
