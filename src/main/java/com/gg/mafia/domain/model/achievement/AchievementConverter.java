package com.gg.mafia.domain.model.achievement;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class AchievementConverter implements AttributeConverter<AchievementEnum, Integer> {
    @Override
    public Integer convertToDatabaseColumn(AchievementEnum achievementEnum) {
        return achievementEnum.getStep();
    }

    @Override
    public AchievementEnum convertToEntityAttribute(Integer value) {
        return AchievementEnum.getByValue(value);
    }
}
