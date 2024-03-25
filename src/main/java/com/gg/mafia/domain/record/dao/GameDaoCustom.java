package com.gg.mafia.domain.record.dao;

import com.gg.mafia.domain.record.domain.Game;
import com.gg.mafia.domain.record.dto.ActionSuccessCountDto;
import com.gg.mafia.domain.record.dto.GameSearchRequest;
import com.gg.mafia.global.common.request.SearchQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

public interface GameDaoCustom {
    Long searchForCount(GameSearchRequest request, SearchQuery searchQuery);

    Page<Game> search(GameSearchRequest request, SearchQuery searchQuery, @NonNull Pageable pageable);

    Integer getActionSuccessCount(ActionSuccessCountDto dto);
}
