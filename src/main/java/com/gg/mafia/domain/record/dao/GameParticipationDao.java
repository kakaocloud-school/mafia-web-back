package com.gg.mafia.domain.record.dao;

import com.gg.mafia.domain.record.domain.GameParticipation;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface GameParticipationDao extends JpaRepository<GameParticipation, Long> {
    @Override
    @NonNull
    Page<GameParticipation> findAll(@NonNull Pageable pageable);

    @Override
    @NonNull
    Optional<GameParticipation> findById(@NonNull Long id);
}
