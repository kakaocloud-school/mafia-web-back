package com.gg.mafia.domain.comment.dao;

import com.gg.mafia.domain.comment.domain.Comment;
import com.gg.mafia.domain.member.domain.QRole;
import com.gg.mafia.domain.member.domain.Role;
import com.gg.mafia.domain.model.RoleEnum;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class CommentDaoImpl implements CommentDao {
    private final JPAQueryFactory queryFactory;

    public CommentDaoImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

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
