package com.gg.mafia.domain.profile.domain;

import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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

public class Profile extends BaseEntity {
    @Column(nullable = false)
    @Builder.Default
    private String description = "";

    @Column(nullable = false)
    @Builder.Default
    private int rating = 0;

    @OneToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    @Builder.Default
    private String userName = "undefined";

    @Column(nullable = false)
    @Builder.Default
    private int ranking = 0;

    @Column(nullable = false)
    @Builder.Default
    private float mafiaOdd = 0;

    @Column(nullable = false)
    @Builder.Default
    private float citizenOdd = 0;

    @Column(nullable = false)
    @Builder.Default
    private float policeOdd = 0;

    @Column(nullable = false)
    @Builder.Default
    private float doctorOdd = 0;

    @Column(nullable = false)
    @Builder.Default
    private float averageOdd = 0;
}
