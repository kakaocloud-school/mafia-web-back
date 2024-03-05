package com.gg.mafia.domain.achievement.domain;

import com.gg.mafia.domain.model.BaseEntity;
import com.gg.mafia.domain.model.achievement.AchievementConverter;
import com.gg.mafia.domain.model.achievement.AchievementEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Achievement extends BaseEntity {
    @Column(unique = true)
    @Convert(converter = AchievementConverter.class)
    private AchievementEnum value;
}
