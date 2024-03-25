package com.gg.mafia.domain.record.domain;

import com.gg.mafia.domain.model.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
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
public class Game extends BaseEntity {
    private Integer round;

    private Boolean mafiaWon;

    @Column(nullable = false)
    private int cureSuccessCount = 0;

    @Column(nullable = false)
    private int killSuccessCount = 0;

    @Column(nullable = false)
    private int detectSuccessCount = 0;

    @OneToMany(mappedBy = "game", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<GameParticipation> gameParticipations = new ArrayList<>();

    @OneToMany(mappedBy = "game", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<GameRound> gameRounds = new ArrayList<>();

    @Builder
    public Game(Integer round, Boolean mafiaWon) {
        this.round = round;
        this.mafiaWon = mafiaWon;
    }

    public void increaseCureSuccessCount() {
        cureSuccessCount++;
    }

    public void increaseKillSuccessCount() {
        killSuccessCount++;
    }

    public void increaseDetectSuccessCount() {
        detectSuccessCount++;
    }
}