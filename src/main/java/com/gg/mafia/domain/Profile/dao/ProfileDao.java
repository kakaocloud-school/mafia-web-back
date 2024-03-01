package com.gg.mafia.domain.Profile.dao;

import com.gg.mafia.domain.Profile.domain.ProfileEntity;
import com.gg.mafia.domain.member.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface ProfileDao extends JpaRepository<ProfileEntity, Long> {
    @Override
    @NonNull
    Optional<ProfileEntity> findById(@NonNull Long Long);

    @NonNull
    Optional<ProfileEntity> findByUser(@NonNull User user);

}
