package com.gg.mafia.domain.achievement.domain;

import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.model.BaseEntity;
import com.gg.mafia.domain.record.domain.JobEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString(of = {"commonAchieveStep", "mafiaAchieveStep", "citizenAchieveStep", "policeAchieveStep",
        "doctorAchieveStep"})
public class AchievementStep extends BaseEntity {
    @OneToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    private Integer commonAchieveStep = 1;

    private Integer mafiaAchieveStep = 1;

    private Integer citizenAchieveStep = 1;

    private Integer policeAchieveStep = 1;

    private Integer doctorAchieveStep = 1;

    @Builder
    public AchievementStep(User user) {
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

    public Map<JobEnum, Integer> getStepsToMap() {
        Map<JobEnum, Integer> steps = new HashMap<>();
        steps.put(JobEnum.COMMON, this.commonAchieveStep);
        steps.put(JobEnum.MAFIA, this.mafiaAchieveStep);
        steps.put(JobEnum.CITIZEN, this.citizenAchieveStep);
        steps.put(JobEnum.POLICE, this.policeAchieveStep);
        steps.put(JobEnum.DOCTOR, this.doctorAchieveStep);
        return steps;
    }

    public void increase(JobEnum jobEnum) {
        if (jobEnum == null) {
            throw new IllegalArgumentException();
        }
        switch (jobEnum.getValue()) {
            case 4:
                this.commonAchieveStep++;
                return;
            case 1:
                this.mafiaAchieveStep++;
                return;
            case 0:
                this.citizenAchieveStep++;
                return;
            case 3:
                this.policeAchieveStep++;
                return;
            case 2:
                this.doctorAchieveStep++;
                return;
            default:
                throw new IllegalArgumentException();
        }
    }
}
