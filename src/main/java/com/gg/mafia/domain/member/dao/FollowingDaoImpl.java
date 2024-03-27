package com.gg.mafia.domain.member.dao;

import com.gg.mafia.domain.member.domain.Following;
import com.gg.mafia.domain.member.domain.QFollowing;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FollowingDaoImpl implements FollowingDaoCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Following> findByFollower(Long followerId) {
        QFollowing following = QFollowing.following;
        return queryFactory
                .selectFrom(following)
                .where(following.follower.id.eq(followerId))
                .fetch();
    }

    @Override
    public List<Following> findByFollowee(Long followeeId) {
        QFollowing following = QFollowing.following;
        return queryFactory
                .selectFrom(following)
                .where(following.followee.id.eq(followeeId))
                .fetch();
    }
}
