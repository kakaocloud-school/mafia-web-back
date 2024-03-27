package com.gg.mafia.domain.profile.api;

import com.gg.mafia.domain.profile.application.ProfileService;
import com.gg.mafia.domain.profile.dto.RankResponse;
import com.gg.mafia.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class ProfileApi {
    private final ProfileService profileService;

    @GetMapping("/ranks")
    public ResponseEntity<ApiResponse<Page<RankResponse>>> getAllRanks(@PageableDefault Pageable pageable) {
        Page<RankResponse> result = profileService.getAllRanks(pageable);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}