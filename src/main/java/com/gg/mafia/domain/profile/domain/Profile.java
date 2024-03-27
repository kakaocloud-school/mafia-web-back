package com.gg.mafia.domain.profile.domain;

import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Profile extends BaseEntity {
    @Column(nullable = false)
    private String description = "안녕하세요";

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private int rating = 0;

    @Column(nullable = false)
    private float mafiaWinningRate = 0;

    @Column(nullable = false)
    private float citizenWinningRate = 0;

    @Column(nullable = false)
    private float policeWinningRate = 0;

    @Column(nullable = false)
    private float doctorWinningRate = 0;

    @Column(nullable = false)
    private float winningRate = 0;

    @Builder
    public Profile(User user, String userName) {
        setUser(user);
        this.userName = userName;
    }

    public Long getUserId() {
        return user.getId();
    }

    private void setUser(User user) {
        if (this.user != null) {
            user.setProfile(null);
        }
        this.user = user;
        user.setProfile(this);
    }
}