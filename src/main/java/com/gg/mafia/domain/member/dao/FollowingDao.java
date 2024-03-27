package com.gg.mafia.domain.member.dao;

import com.gg.mafia.domain.member.domain.Following;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowingDao extends JpaRepository<Following, Long>, FollowingDaoCustom {
    List<Following> findByFollower(Long followerId);

    List<Following> findByFolloweeId(Long followerId);

    List<Following> findByFollowee(Long followeeId);

    List<Following> findByFollowerId(Long followerId);
}
