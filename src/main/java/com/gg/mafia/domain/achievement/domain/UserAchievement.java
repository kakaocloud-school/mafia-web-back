package com.gg.mafia.domain.achievement.domain;

import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.model.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"user_id", "achievement_id"}
                )
        }
)
@ToString(of = {"user", "achievement"})
public class UserAchievement extends BaseEntity {
    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Achievement achievement;

    @Builder
    public UserAchievement(User user, Achievement achievement) {
        setUser(user);
        this.achievement = achievement;
    }

    public static UserAchievement relate(User user, Achievement achievement) {
        return UserAchievement.builder()
                .user(user)
                .achievement(achievement)
                .build();
    }

    public void setUser(User user) {
        if (this.user != null) {
            this.user.getUserAchievements().remove(this);
        }
        this.user = user;
        this.user.getUserAchievements().add(this);
    }
}
