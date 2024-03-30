package com.gg.mafia.domain.record.application;

import com.gg.mafia.domain.record.dao.GameHistoryDao;
import com.gg.mafia.domain.record.dto.GameHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameHistoryService {
    private final GameHistoryDao gameHistoryDao;

    public Page<GameHistoryResponse> getAll(Long userId, Pageable pageable) {
        return gameHistoryDao.findAll(userId, pageable);
    }
}
