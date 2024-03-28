package com.gg.mafia.domain.achievement.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserAchievementImpl implements UserAchievementDaoCustom {
    private final JPAQueryFactory queryFactory;
}
