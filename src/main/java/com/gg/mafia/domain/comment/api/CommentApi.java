package com.gg.mafia.domain.comment.api;

import com.gg.mafia.domain.comment.application.CommentService;
import com.gg.mafia.domain.comment.domain.Comment;
import com.gg.mafia.global.common.response.ApiResponse;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentApi {
    private final CommentService commentServcice;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Comment>>> showCommentsByProfile(Principal principal,
                                                                            @RequestParam Long profileId) {
        // 프로필 별 댓글을 조회한다.
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Comment>>> showCommentsByUser(Principal principal,
                                                                         @RequestParam Long userId) {
        // 사용자 별 댓글을 조회한다.
    }

    @PostMapping
    public void saveComment() {
        // 댓글을 저장한다.
    }

    @PatchMapping
    public void updateComment() {
        // 댓글을 수정한다.
    }

    @DeleteMapping
    public void removeComment() {
        // 댓글을 삭제한다.

    }
}
