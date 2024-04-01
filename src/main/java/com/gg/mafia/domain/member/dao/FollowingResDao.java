package com.gg.mafia.domain.member.dao;

import com.gg.mafia.domain.member.domain.QFollowing;
import com.gg.mafia.domain.member.dto.FollowingResponse;
import com.gg.mafia.domain.profile.domain.QProfile;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FollowingResDao {
    private final JPAQueryFactory queryFactory;

    public Page<FollowingResponse> findByFollowerId(Long followerId, Pageable pageable) {
        QFollowing following = QFollowing.following;
        QProfile profile = QProfile.profile;

        List<FollowingResponse> followingInfo = queryFactory
                .select(Projections.constructor(FollowingResponse.class, following.id, following.followee.id)
                ).from(following)
                .where(following.follower.id.eq(followerId))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        Map<Long, Long> followMap = followingInfo.stream()
                .collect(Collectors.toMap(FollowingResponse::getFollowUserId, FollowingResponse::getFollowingId));

        List<FollowingResponse> result = queryFactory
                .select(Projections.constructor(FollowingResponse.class, profile.user.id, profile.imageUrl,
                        profile.userName)
                )
                .from(profile)
                .where(profile.user.id.in(followMap.keySet()))
                .fetch();
        result.stream().forEach(e -> {
                    Long followingId = followMap.get(e.getFollowUserId());
                    e.setFollowingId(followingId);
                }
        );
        Long count = searchFroCount(followerId);
        return new PageImpl<>(result, pageable, count);

    }

    public Page<FollowingResponse> findByFolloweeId(Long followeeId, Pageable pageable) {
        QFollowing following = QFollowing.following;
        QProfile profile = QProfile.profile;

        List<FollowingResponse> followingInfo = queryFactory
                .select(Projections.constructor(FollowingResponse.class, following.id, following.follower.id)
                ).from(following)
                .where(following.followee.id.eq(followeeId))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        Map<Long, Long> followMap = followingInfo.stream()
                .collect(Collectors.toMap(FollowingResponse::getFollowUserId, FollowingResponse::getFollowingId));

        List<FollowingResponse> result = queryFactory
                .select(Projections.constructor(FollowingResponse.class, profile.user.id, profile.imageUrl,
                        profile.userName)
                )
                .from(profile)
                .where(profile.user.id.in(followMap.keySet()))
                .fetch();
        result.stream().forEach(e -> {
                    Long followingId = followMap.get(e.getFollowUserId());
                    e.setFollowingId(followingId);
                }
        );
        Long count = searchFroCount(followeeId);
        return new PageImpl<>(result, pageable, count);
    }

    public Long searchFroCount(Long followerId) {
        QFollowing following = QFollowing.following;

        Long count = queryFactory
                .select(following.count()).from(following)
                .where(following.follower.id.eq(followerId))
                .fetchOne();
        if (count == null) {
            count = 0L;
        }
        return count;
    }
}
