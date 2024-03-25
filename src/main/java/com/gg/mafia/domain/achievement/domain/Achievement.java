package com.gg.mafia.domain.achievement.domain;

import com.gg.mafia.domain.model.BaseEntity;
import com.gg.mafia.domain.record.domain.JobEnum;
import com.gg.mafia.domain.record.domain.JobEnumConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(of = {"achievementName", "jobName"})
public class Achievement extends BaseEntity {
    @Column(unique = true)
    @Convert(converter = AchievementConverter.class)
    private AchievementEnum achievementName;
    @Convert(converter = JobEnumConverter.class)
    private JobEnum jobName;
}
