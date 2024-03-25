package com.gg.mafia.domain.record.dao;

import com.gg.mafia.domain.record.domain.Game;
import com.gg.mafia.domain.record.dto.ActionSuccessCountDto;
import com.gg.mafia.domain.record.dto.GameSearchRequest;
import com.gg.mafia.global.common.request.SearchQuery;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface GameDao extends JpaRepository<Game, Long>, GameDaoCustom {
    @Override
    @NonNull
    Page<Game> findAll(@NonNull Pageable pageable);

    @Override
    Long searchForCount(GameSearchRequest request, SearchQuery searchQuery);

    @Override
    Page<Game> search(GameSearchRequest request, SearchQuery searchQuery, @NonNull Pageable pageable);

    @Override
    Integer getActionSuccessCount(ActionSuccessCountDto dto);

    @Override
    @NonNull
    Optional<Game> findById(@NonNull Long id);
}
