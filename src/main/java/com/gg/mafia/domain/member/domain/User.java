package com.gg.mafia.domain.member.domain;

import com.gg.mafia.domain.model.BaseEntity;
import com.gg.mafia.domain.profile.domain.Profile;
import com.gg.mafia.domain.record.domain.GameParticipation;
import com.gg.mafia.domain.profile.domain.Profile;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class User extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST,
            CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserToRole> userToRoles = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST,
            CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserAchievement> userAchievements = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST,
            CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private AchievementStep achievementStep;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<GameParticipation> gameParticipations = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST,
            CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private Profile profile;
    @Builder
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}