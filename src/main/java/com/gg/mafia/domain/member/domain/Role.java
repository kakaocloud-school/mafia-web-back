package com.gg.mafia.domain.member.domain;

import com.gg.mafia.domain.model.BaseEntity;
import com.gg.mafia.domain.model.RoleEnum;
import com.gg.mafia.domain.model.RoleEnumConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
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
public class Role extends BaseEntity {
    @Column(unique = true)
    @Convert(converter = RoleEnumConverter.class)
    private RoleEnum value;

    @OneToMany(mappedBy = "role", cascade = {CascadeType.PERSIST,
            CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserToRole> userToRoles = new ArrayList<>();
}