package com.gg.mafia.domain.record.dao;

import com.gg.mafia.domain.record.domain.GameRound;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface GameRoundDao extends JpaRepository<GameRound, Long> {
    @Override
    @NonNull
    Page<GameRound> findAll(@NonNull Pageable pageable);

    @Override
    @NonNull
    Optional<GameRound> findById(@NonNull Long id);
}
