package com.gg.mafia.domain.member.api;

import com.gg.mafia.domain.member.application.FollowingService;
import com.gg.mafia.domain.member.domain.Following;
import com.gg.mafia.domain.member.dto.FollowingResponse;
import com.gg.mafia.global.common.response.ApiResponse;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class FollowingApi {
    private final FollowingService followingService;

    @GetMapping("/users/{userId}/followees")
    public ResponseEntity<ApiResponse<Page<FollowingResponse>>> showFollowees(Principal principal,
                                                                              @PathVariable Long userId,
                                                                              @PageableDefault Pageable pageable) {
        Page<FollowingResponse> followees = followingService.getFollowees(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(followees));
    }

    @GetMapping("/users/{userId}/followers")
    public ResponseEntity<ApiResponse<List<Following>>> showFollowers(Principal principal,
                                                                      @PathVariable Long userId) {
        List<Following> followees = followingService.getFollowers(userId);
        return ResponseEntity.ok(ApiResponse.success(followees));
    }

    @PostMapping("/follow")
    public ResponseEntity<ApiResponse<Void>> addFollow(Principal principal, @RequestBody String followeeEmail) {
        followingService.insertFollowing(principal.getName(), followeeEmail);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/follow")
    public ResponseEntity<ApiResponse<Void>> removeFollow(Principal principal, @RequestBody String followeeEmail) {
        followingService.removeFollowee(principal.getName(), followeeEmail);
        return ResponseEntity.ok().build();
    }
}
