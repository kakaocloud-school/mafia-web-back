package com.gg.mafia.domain.achievement.domain;

import com.gg.mafia.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Achievement extends BaseEntity {
    @Column(nullable = false)
    private String name;
}
