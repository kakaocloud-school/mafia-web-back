package com.gg.mafia.domain.board.domain;

import com.gg.mafia.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Sample extends BaseEntity {
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;
}
