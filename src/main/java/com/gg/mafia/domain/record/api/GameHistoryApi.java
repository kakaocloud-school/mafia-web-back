package com.gg.mafia.domain.record.api;

import com.gg.mafia.domain.record.application.GameHistoryService;
import com.gg.mafia.domain.record.dto.GameHistoryResponse;
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
@RequestMapping("/record/users")
@RequiredArgsConstructor
public class GameHistoryApi {
    private final GameHistoryService gameHistoryService;

    @GetMapping("/{userId}/histories")
    public ResponseEntity<ApiResponse<Page<GameHistoryResponse>>> getAll(Long userId,
                                                                         @PageableDefault Pageable pageable) {
        Page<GameHistoryResponse> result = gameHistoryService.getAll(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
