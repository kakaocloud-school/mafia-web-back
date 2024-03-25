package com.gg.mafia.domain.record.domain;

import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.model.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
                        columnNames = {"round", "game_id"}
                )
        }
)
public class GameRound extends BaseEntity {
    @Column(nullable = false)
    private Integer round;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Game game;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User votedPlayer;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User killedPlayer;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User curedPlayer;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User detectedPlayer;


    @Builder
    public GameRound(Integer round, Game game, User votedPlayer, User killedPlayer, User curedPlayer,
                     User detectedPlayer) {
        this.round = round;
        this.votedPlayer = votedPlayer;
        this.killedPlayer = killedPlayer;
        this.curedPlayer = curedPlayer;
        this.detectedPlayer = detectedPlayer;
        setGame(game);
    }

    public void setGame(Game game) {
        if (this.game != null) {
            this.game.getGameRounds().remove(this);
        }
        this.game = game;
        this.game.getGameRounds().add(this);
    }
}