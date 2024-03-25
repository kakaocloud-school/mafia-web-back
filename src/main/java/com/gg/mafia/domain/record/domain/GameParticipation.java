package com.gg.mafia.domain.record.domain;

import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.model.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"user_id", "game_id"}
                )
        }
)
public class GameParticipation extends BaseEntity {
    @Convert(converter = JobEnumConverter.class)
    private JobEnum job;

    private Boolean survival;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Game game;

    @OneToMany(mappedBy = "gameParticipation", cascade = {CascadeType.PERSIST,
            CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<PlayerAction> playerActions = new ArrayList<>();

    @Builder
    public GameParticipation(JobEnum job, Boolean survival, User user, Game game) {
        this.job = job;
        this.survival = survival;
        setUser(user);
        setGame(game);
    }

    public void setUser(User user) {
        if (this.user != null) {
            this.user.getGameParticipations().remove(this);
        }
        this.user = user;
        this.user.getGameParticipations().add(this);
    }

    public void setGame(Game game) {
        if (this.game != null) {
            this.game.getGameParticipations().remove(this);
        }
        this.game = game;
        this.game.getGameParticipations().add(this);
    }
}