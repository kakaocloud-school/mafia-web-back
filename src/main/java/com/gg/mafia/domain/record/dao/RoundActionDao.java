package com.gg.mafia.domain.record.dao;

import com.gg.mafia.domain.record.domain.RoundAction;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface RoundActionDao extends JpaRepository<RoundAction, Long> {
    @Override
    @NonNull
    Page<RoundAction> findAll(@NonNull Pageable pageable);

    @Override
    @NonNull
    Optional<RoundAction> findById(@NonNull Long id);
}
