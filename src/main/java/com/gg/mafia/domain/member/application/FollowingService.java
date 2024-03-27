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
    public Following insertFollowing(String followerEmail, String followeeEmail) {
        Following following = createFollowing(followerEmail, followeeEmail);
        return followingDao.save(following);
    }

    public List<Following> getFollowees(String followerEmail) {
        return followingDao.findByFollowerId(getUser(followerEmail).getId());
    }

    public List<Following> getFollowers(String followeeEmail) {
        return followingDao.findByFolloweeId(getUser(followeeEmail).getId());
    }

    @Transactional
    public void removeFollowee(String followerEmail, String followeeEmail) {
        Long followerId = getUser(followerEmail).getId();
        Long followeeId = getUser(followeeEmail).getId();

        Following following = followingDao.findByFollowerIdAndFolloweeId(followerId, followeeId)
                .orElseThrow(() -> new IllegalArgumentException());

        followingDao.delete(following);
    }

    private User getUser(String userEmail) {
        return userDao.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException());
    }

    private Following createFollowing(String followerEmail, String followeeEmail) {
        User follower = getUser(followerEmail);
        User followee = getUser(followeeEmail);

        return Following.builder().follower(follower).followee(followee).build();
    }
}
