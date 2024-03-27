package com.gg.mafia.domain.member.api;

import com.gg.mafia.domain.member.application.FollowingService;
import com.gg.mafia.domain.member.domain.Following;
import com.gg.mafia.global.common.response.ApiResponse;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class FollowingApi {
    private final FollowingService followingService;

    @GetMapping("/follow/followees")
    public ResponseEntity<ApiResponse<List<Following>>> showFollowees(Principal principal,
                                                                      @RequestParam String email) {
        List<Following> followees = followingService.getFollowees(email);
        return ResponseEntity.ok(ApiResponse.success(followees));
    }

    @GetMapping("/follow/followers")
    public ResponseEntity<ApiResponse<List<Following>>> showFollowers(Principal principal,
                                                                      @RequestParam String email) {
        List<Following> followees = followingService.getFollowees(email);
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
