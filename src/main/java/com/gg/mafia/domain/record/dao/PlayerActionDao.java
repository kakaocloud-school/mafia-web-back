package com.gg.mafia.domain.record.dao;

import com.gg.mafia.domain.record.domain.PlayerAction;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface PlayerActionDao extends JpaRepository<PlayerAction, Long> {
    @Override
    @NonNull
    Page<PlayerAction> findAll(@NonNull Pageable pageable);

    @Override
    @NonNull
    Optional<PlayerAction> findById(@NonNull Long id);
}
