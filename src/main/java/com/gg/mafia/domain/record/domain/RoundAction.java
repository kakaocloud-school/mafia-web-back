package com.gg.mafia.domain.record.domain;

import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.model.BaseEntity;
import jakarta.persistence.CascadeType;
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
                        columnNames = {"round", "gameParticipation_id"}
                )
        }
)
public class RoundAction extends BaseEntity {
    private Integer round;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private GameParticipation gameParticipation;

    private boolean survival;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User firstVote;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User secondVote;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User actionTarget;


    @Builder
    public RoundAction(Integer round, GameParticipation gameParticipation, boolean survival, User firstVote,
                       User secondVote, User actionTarget) {
        this.round = round;
        this.survival = survival;
        this.firstVote = firstVote;
        this.secondVote = secondVote;
        this.actionTarget = actionTarget;
        setGameParticipation(gameParticipation);
    }

    public void setGameParticipation(GameParticipation gameParticipation) {
        if (this.gameParticipation != null) {
            this.gameParticipation.getRoundActions().remove(this);
        }
        this.gameParticipation = gameParticipation;
        this.gameParticipation.getRoundActions().add(this);
    }
}