package com.gg.mafia.domain.achievement.dao;

import com.gg.mafia.domain.achievement.domain.Achievement;
import com.gg.mafia.domain.achievement.domain.AchievementEnum;
import com.gg.mafia.domain.achievement.domain.UserAchievement;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

public interface AchievementDao extends JpaRepository<Achievement, Long>, AchievementDaoCustom {
    @Override
    @NonNull
    Page<Achievement> findAll(@NonNull Pageable pageable);

    @Override
    @NonNull
    Optional<Achievement> findById(@NonNull Long id);

    @Override
    Optional<Achievement> findByAchieveName(AchievementEnum achievementEnum);

    @Query("SELECT COUNT(*) FROM Achievement a")
    Long getAchievementCount();

    @Query(value = "SELECT u FROM UserAchievement u left join fetch u.user f")
    List<UserAchievement> findAchieve();

    Optional<Achievement> findByAchievementName(AchievementEnum achievementEnum);
}
