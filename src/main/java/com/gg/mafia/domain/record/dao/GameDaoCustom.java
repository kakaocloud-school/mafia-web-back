package com.gg.mafia.domain.record.dao;

import com.gg.mafia.domain.record.domain.Game;
import com.gg.mafia.domain.record.dto.GameSearchRequest;
import com.gg.mafia.global.common.request.SearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

public interface GameDaoCustom {
    Page<Game> search(GameSearchRequest request, SearchFilter filter, @NonNull Pageable pageable);
}
