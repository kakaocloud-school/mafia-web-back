package com.gg.mafia.domain.Profile.domain;

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

public class ProfileEntity extends BaseEntity {
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int rating;

    @OneToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private int ranking;

    @Column(nullable = false)
    private float mafiaOdd;

    @Column(nullable = false)
    private float citizenOdd;

    @Column(nullable = false)
    private float policeOdd;

    @Column(nullable = false)
    private float doctorOdd;
    
    @Column(nullable = false)
    private float averageOdd;
}
