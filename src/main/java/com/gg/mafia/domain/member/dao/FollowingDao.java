package com.gg.mafia.domain.member.dao;

import com.gg.mafia.domain.member.domain.Following;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowingDao extends JpaRepository<Following, Long>, FollowingDaoCustom {
    List<Following> findByFolloweeId(Long followerId);

    List<Following> findByFollowerId(Long followerId);

    Optional<Following> findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);
}
