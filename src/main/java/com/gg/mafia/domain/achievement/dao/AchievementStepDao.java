package com.gg.mafia.domain.achievement.dao;

import com.gg.mafia.domain.achievement.domain.AchievementStep;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface AchievementStepDao extends JpaRepository<AchievementStep, Long> {
    @Override
    @NonNull
    Optional<AchievementStep> findById(@NonNull Long id);

    Optional<AchievementStep> findByUserId(@NonNull Long userId);
}
