package com.gg.mafia.domain.member.application;


import com.gg.mafia.domain.member.dao.FollowingDao;
import com.gg.mafia.domain.member.dao.UserDao;
import com.gg.mafia.domain.member.domain.Following;
import com.gg.mafia.domain.member.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowingService {
    private final FollowingDao followingDao;
    private final UserDao userDao;

    @Transactional
    public void insertFollowing(Long followerId, Long followeeId) {
        followingDao.save(createFollowing(followerId, followeeId));
    }

    public List<Following> getFollowees(Long followerId) {
        return followingDao.findByFollowerId(followerId);
    }

    public List<Following> getFollowers(Long followeeId) {
        return followingDao.findByFollowerId(followeeId);
    }

    @Transactional
    public void removeFollowee(Long followerId, Long followeeId) {
        Following following = followingDao.findByFollowerIdAndFolloweeId(followerId, followeeId)
                .orElseThrow(() -> new IllegalArgumentException());
        followingDao.delete(following);
    }

    private Following createFollowing(Long followerId, Long followeeId) {
        User follower = userDao.findById(followerId).orElseThrow(() -> new IllegalArgumentException());
        User followee = userDao.findById(followeeId).orElseThrow(() -> new IllegalArgumentException());

        return Following.builder().follower(follower).followee(followee).build();
    }
}
