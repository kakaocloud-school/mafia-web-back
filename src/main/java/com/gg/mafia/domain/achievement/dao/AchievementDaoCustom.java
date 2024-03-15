package com.gg.mafia.domain.achievement.dao;

import com.gg.mafia.domain.achievement.domain.Achievement;
import com.gg.mafia.domain.achievement.domain.AchievementEnum;
import java.util.Optional;

public interface AchievementDaoCustom {
    Optional<Achievement> findByAchieveName(AchievementEnum achievementEnum);
}
