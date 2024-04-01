package com.gg.mafia.domain.profile.dao;

import com.gg.mafia.domain.profile.domain.Profile;
import com.gg.mafia.domain.profile.dto.ProfileSearchRequest;
import com.gg.mafia.global.common.request.SearchQuery;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface ProfileDao extends JpaRepository<Profile, Long>, ProfileDaoCustom {
    @Override
    Optional<Profile> findByUserId(Long Id);

    @Override
    Page<Profile> search(ProfileSearchRequest dto, SearchQuery searchQuery, @NonNull Pageable pageable);

    @Override
    @NonNull
    Page<Profile> findAll(@NonNull Pageable pageable);

    @Override
    Page<Profile> findAllRanks(Pageable pageable);
}