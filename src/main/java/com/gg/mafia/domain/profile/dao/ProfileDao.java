package com.gg.mafia.domain.profile.dao;

import com.gg.mafia.domain.profile.domain.Profile;
import com.gg.mafia.domain.member.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface ProfileDao extends JpaRepository<Profile, Long> {
    @Override
    @NonNull
    Optional<Profile> findById(@NonNull Long Id);

    @NonNull
    Page<Profile> findByUserName(@NonNull String userName, Pageable pageable);

    @Override
    @NonNull
    Page<Profile> findAll(@NonNull Pageable pageable);

}
