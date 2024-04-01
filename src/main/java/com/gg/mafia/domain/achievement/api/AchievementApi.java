package com.gg.mafia.domain.achievement.api;

import com.gg.mafia.domain.achievement.application.AchievementService;
import com.gg.mafia.domain.achievement.domain.Achievement;
import com.gg.mafia.global.common.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/achievement")
public class AchievementApi {
    private final AchievementService achievementService;

    ///achievement/users/10/achievements/1
    @GetMapping("/users/{userId}/achievements")
    public ResponseEntity<ApiResponse<List<Achievement>>> showUserAchievements(@PathVariable Long userId) {
        List<Achievement> achievements = achievementService.getUserAchievementByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(achievements));
    }
}
