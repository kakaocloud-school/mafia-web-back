package com.gg.mafia.domain.achievement.dao;

import com.gg.mafia.domain.achievement.domain.Achievement;
import com.gg.mafia.domain.achievement.domain.AchievementEnum;
import com.gg.mafia.domain.achievement.domain.QAchievement;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AchievementDaoImpl implements AchievementDaoCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Achievement> findByAchieveName(AchievementEnum achievementEnum) {
        QAchievement achievement = QAchievement.achievement;
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(achievement)
                        .where(achievement.achievementName.eq(achievementEnum))
                        .fetchOne()
        );
    }
}
