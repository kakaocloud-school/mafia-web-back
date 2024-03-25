package com.gg.mafia.domain.comment.dao;

import com.gg.mafia.domain.comment.domain.Comment;
import com.gg.mafia.domain.member.domain.QRole;
import com.gg.mafia.domain.member.domain.Role;
import com.gg.mafia.domain.model.RoleEnum;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.Optional;

public class CommentDaoImpl implements CommentDaoCustom {
    private final JPAQueryFactory queryFactory;

    public CommentDaoImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<Comment> findByValue(RoleEnum value) {
        QRole role = QRole.role;
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(comment)
                        .where(comment.eql(user))
                        .fetchOne()
        );
    }
}
