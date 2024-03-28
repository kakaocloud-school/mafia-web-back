package com.gg.mafia.domain.achievement.domain;

@FunctionalInterface
public interface AchievementLogic<T> {
    T logic();
}
