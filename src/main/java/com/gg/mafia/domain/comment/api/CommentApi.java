package com.gg.mafia.domain.comment.api;

import com.gg.mafia.domain.comment.application.CommentService;
import com.gg.mafia.domain.comment.domain.Comment;
import com.gg.mafia.domain.comment.dto.CommentRequest;
import com.gg.mafia.global.common.response.ApiResponse;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentApi {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Comment>>> showCommentsByProfile(Principal principal,
                                                                            @RequestParam Long profileId) {
        List<Comment> comments = commentService.selectCommentByProfileId(profileId);
        return ResponseEntity.ok(ApiResponse.success(comments));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> saveComment(Principal principal, @RequestBody CommentRequest request) {
        commentService.insertComment(principal.getName(), request.getProfileId(),
                request.getContent());
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<Void>> updateComment(Principal principal, @RequestBody CommentRequest request) {
        commentService.updateComment(principal.getName(), request.getCommentId(), request.getUpdateContent());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> removeComment(Principal principal, @RequestBody CommentRequest request) {
        commentService.deleteComment(request.getCommentId());
        return ResponseEntity.ok().build();

    }
}
