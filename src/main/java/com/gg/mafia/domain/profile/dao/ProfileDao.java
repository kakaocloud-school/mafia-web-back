package com.gg.mafia.domain.profile.dao;

import com.gg.mafia.domain.profile.domain.Profile;
import com.gg.mafia.domain.record.domain.Game;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface ProfileDao extends JpaRepository<Game, Long>, ProfileDaoCustom {
    @Override
    @NonNull
    Page<Game> findAll(@NonNull Pageable pageable);

    @Override
    @NonNull
    Optional<Game> findById(@NonNull Long id);

    @Override
    Page<Profile> findAllRanks(Pageable pageable);
}
