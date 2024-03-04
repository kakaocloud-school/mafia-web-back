package com.gg.mafia.domain.achievement.domain;

import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AchievementStep extends BaseEntity {
    @OneToOne(mappedBy = "achievementStep")
    private User user;
    private Integer commonAchieveStep;
    private Integer mafiaAchieveStep;
    private Integer citizenAchieveStep;
    private Integer policeAchieveStep;
    private Integer doctorAchieveStep;

    public static AchievementStep create() {
        return AchievementStep.builder()
                .commonAchieveStep(1)
                .mafiaAchieveStep(1)
                .citizenAchieveStep(1)
                .policeAchieveStep(1)
                .doctorAchieveStep(1)
                .build();
    }
}
