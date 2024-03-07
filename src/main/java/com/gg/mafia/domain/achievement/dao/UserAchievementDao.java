package com.gg.mafia.domain.achievement.dao;

import com.gg.mafia.domain.achievement.domain.UserAchievement;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface UserAchievementDao extends JpaRepository<UserAchievement, Long> {
    @Override
    @NonNull
    Optional<UserAchievement> findById(@NonNull Long id);

    Optional<UserAchievement> findByUser_id(@NonNull Long user_id);

    Optional<UserAchievement> findByAchievement_id(@NonNull Long achievement_id);
}
