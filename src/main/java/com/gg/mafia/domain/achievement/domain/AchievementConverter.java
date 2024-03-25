package com.gg.mafia.domain.achievement.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class AchievementConverter implements AttributeConverter<AchievementEnum, String> {
    @Override
    public String convertToDatabaseColumn(AchievementEnum achievementEnum) {
        return achievementEnum.getName();
    }

    @Override
    public AchievementEnum convertToEntityAttribute(String value) {
        return AchievementEnum.getByValue(value);
    }
}
