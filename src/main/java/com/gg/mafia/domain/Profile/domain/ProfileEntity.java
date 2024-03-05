package com.gg.mafia.domain.Profile.domain;

import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    //profile의 외래키열의 이름은 user_id 참조하는 열은 id라 생각해서 작성
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;
}
