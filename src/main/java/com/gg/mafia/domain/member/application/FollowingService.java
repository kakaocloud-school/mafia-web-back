package com.gg.mafia.domain.member.application;


import com.gg.mafia.domain.member.dao.FollowingDao;
import com.gg.mafia.domain.member.dao.FollowingResDao;
import com.gg.mafia.domain.member.dao.UserDao;
import com.gg.mafia.domain.member.domain.Following;
import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.member.dto.FollowingResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowingService {
    private final FollowingResDao followingResDao;
    private final FollowingDao followingDao;
    private final UserDao userDao;

    @Transactional
    public Following insertFollowing(String followerEmail, String followeeEmail) {
        Following following = createFollowing(followerEmail, followeeEmail);
        return followingDao.save(following);
    }

    public Page<FollowingResponse> getFollowees(String followerEmail, Pageable pageable) {
        return followingResDao.findByFollowerId(getUser(followerEmail).getId(), pageable);
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
