package com.gg.mafia.domain.member.domain;

import com.gg.mafia.domain.model.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Following extends BaseEntity {
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User targetUser;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User sourceUser;

    @Builder
    public Following(User target, User source) {
        this.targetUser = target;
        this.sourceUser = source;
    }

    public static Following relate(User target, User source) {
        return Following.builder().target(target).source(source).build();
    }
}
