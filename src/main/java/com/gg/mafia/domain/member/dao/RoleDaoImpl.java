package com.gg.mafia.domain.member.dao;

import com.gg.mafia.domain.member.domain.QRole;
import com.gg.mafia.domain.member.domain.Role;
import com.gg.mafia.domain.model.RoleEnum;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoleDaoImpl implements RoleDaoCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Role> findByValue(RoleEnum value) {
        QRole role = QRole.role;
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(role)
                        .where(role.value.eq(value))
                        .fetchOne()
        );
    }
}
