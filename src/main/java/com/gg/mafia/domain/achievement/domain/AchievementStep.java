package com.gg.mafia.domain.achievement.domain;

import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.model.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
public class AchievementStep extends BaseEntity {
    @OneToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
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

    public AchievementStep(User user, Integer commonAchieveStep, Integer mafiaAchieveStep, Integer citizenAchieveStep,
                           Integer policeAchieveStep, Integer doctorAchieveStep) {
        this.commonAchieveStep = commonAchieveStep;
        this.mafiaAchieveStep = mafiaAchieveStep;
        this.citizenAchieveStep = citizenAchieveStep;
        this.policeAchieveStep = policeAchieveStep;
        this.doctorAchieveStep = doctorAchieveStep;
        setUser(user);
    }


    public static AchievementStep create(User user) {
        return AchievementStep.builder()
                .user(user)
                .build();
    }

    public void setUser(User user) {
        this.user = user;
        user.setAchievementStep(this);
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
