package com.gg.mafia.domain.achievement.dao;

import com.gg.mafia.domain.achievement.domain.Achievement;
import com.gg.mafia.domain.model.achievement.AchievementEnum;
import java.util.Optional;

public interface AchievementDaoCustom {
    Optional<Achievement> findByValue(AchievementEnum achievementEnum);
}
