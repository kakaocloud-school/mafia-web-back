package com.gg.mafia.domain.Profile.dao;

import com.gg.mafia.domain.Profile.domain.ProfileEntity;
import com.gg.mafia.domain.member.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface ProfileDao extends JpaRepository<ProfileEntity, Long> {
    @Override
    @NonNull
    Optional<ProfileEntity> findById(@NonNull Long Long);

    @NonNull
    Optional<ProfileEntity> findByUser(@NonNull User user);

    @NonNull
    Page<ProfileEntity> findByUserName(@NonNull String userName, Pageable pageble);

    @Override
    @NonNull
    Page<ProfileEntity> findAll(@NonNull Pageable pageable);

}
