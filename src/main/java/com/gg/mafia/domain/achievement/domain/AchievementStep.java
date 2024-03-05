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
    @Builder.Default
    private Integer commonAchieveStep = 1;
    @Builder.Default
    private Integer mafiaAchieveStep = 1;
    @Builder.Default
    private Integer citizenAchieveStep = 1;
    @Builder.Default
    private Integer policeAchieveStep = 1;
    @Builder.Default
    private Integer doctorAchieveStep = 1;

    public static AchievementStep create() {
        return AchievementStep.builder()
                .build();
    }

    public void increase(String achieveName) {
        switch (achieveName) {
            case "common":
                this.commonAchieveStep++;
                return;
            case "mafia":
                this.mafiaAchieveStep++;
                return;
            case "citizen":
                this.citizenAchieveStep++;
                return;
            case "police":
                this.policeAchieveStep++;
                return;
            case "doctor":
                this.doctorAchieveStep++;
                return;
            default:
                throw new IllegalArgumentException();
        }
    }
}
