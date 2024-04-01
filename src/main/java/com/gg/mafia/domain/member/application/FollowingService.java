package com.gg.mafia.domain.member.application;


import com.gg.mafia.domain.member.dao.FollowingDao;
import com.gg.mafia.domain.member.dao.FollowingResDao;
import com.gg.mafia.domain.member.dao.UserDao;
import com.gg.mafia.domain.member.domain.Following;
import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.member.dto.FollowingResponse;
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
    public Following insertFollowing(String followerEmail, Long userId) {
        Following following = createFollowing(followerEmail, userId);
        return followingDao.save(following);
    }

    public Page<FollowingResponse> getFollowees(Long followerId, Pageable pageable) {
        return followingResDao.findByFollowerId(getUserById(followerId).getId(), pageable);
    }

    public Page<FollowingResponse> getFollowers(Long followeeId, Pageable pageable) {
        return followingResDao.findByFolloweeId(getUserById(followeeId).getId(), pageable);
    }

    private User getUserById(Long followerId) {
        return userDao.findById(followerId).orElseThrow(() -> new IllegalArgumentException());
    }

    @Transactional
    public void removeFollowee(String followerEmail, Long userId) {
        Long followerId = getUser(followerEmail).getId();
        Long followeeId = getUserById(userId).getId();

        Following following = followingDao.findByFollowerIdAndFolloweeId(followerId, followeeId)
                .orElseThrow(() -> new IllegalArgumentException());

        followingDao.delete(following);
    }

    private User getUser(String userEmail) {
        return userDao.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException());
    }

    private Following createFollowing(String followerEmail, Long userId) {
        User follower = getUser(followerEmail);
        User followee = getUserById(userId);

        return Following.builder().follower(follower).followee(followee).build();
    }
}
