package com.gg.mafia.domain.member.dao;

import com.gg.mafia.domain.member.domain.Following;
import java.util.List;

public interface FollowingDaoCustom {
    List<Following> findByFollower(Long followerId);

    List<Following> findByFollowee(Long followeeId);
}
