package com.gg.mafia.domain.member.domain;

import com.gg.mafia.domain.achievement.domain.AchievementStep;
import com.gg.mafia.domain.achievement.domain.UserAchievement;
import com.gg.mafia.domain.model.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST,
            CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserToRole> userToRoles = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST,
            CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserAchievement> userAchievements = new ArrayList<>();

    @OneToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "achievementStep_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private AchievementStep achievementStep;
}