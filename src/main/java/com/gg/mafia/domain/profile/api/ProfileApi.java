package com.gg.mafia.domain.profile.api;

import com.gg.mafia.domain.profile.application.ProfileService;
import com.gg.mafia.domain.profile.dto.ProfileResponse;
import com.gg.mafia.domain.profile.dto.ProfileSearchRequest;
import com.gg.mafia.domain.profile.dto.ProfileUpdateRequest;
import com.gg.mafia.domain.profile.dto.RankResponse;
import com.gg.mafia.domain.profile.dto.RatingRequest;
import com.gg.mafia.global.common.request.SearchQuery;
import com.gg.mafia.global.common.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class ProfileApi {
    @Autowired
    ProfileService profileService;

    @GetMapping(value = "/users/{id}/profile")
    public ProfileResponse getByUserId(@PathVariable("id") Long id) {
        return profileService.getByUserId(id);
    }

    @GetMapping("/ranks")
    public ResponseEntity<ApiResponse<Page<RankResponse>>> getAllRanks(@PageableDefault Pageable pageable) {
        Page<RankResponse> result = profileService.getAllRanks(pageable);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ProfileResponse>>> search(
            ProfileSearchRequest request,
            SearchQuery searchQuery,
            @PageableDefault Pageable pageable
    ) {
        Page<ProfileResponse> result = profileService.search(request, searchQuery, pageable);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PatchMapping(value = "/users/{userId}/profile")
    @PreAuthorize("authentication.principal.getId() == #userId")
    public ResponseEntity<Object> updateProfile(
            @PathVariable("userId") Long userId,
            @Validated @RequestBody ProfileUpdateRequest request
    ) {
        profileService.updateProfile(userId, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/update-rating")
    public ResponseEntity<Object> patchRating(@RequestBody RatingRequest ratingRequest) {
        profileService.patchRating(ratingRequest);
        return ResponseEntity.ok().build();
    }
}
