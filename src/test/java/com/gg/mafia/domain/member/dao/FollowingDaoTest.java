package com.gg.mafia.domain.member.dao;

import com.gg.mafia.domain.member.domain.Following;
import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.global.config.AppConfig;
import com.gg.mafia.global.config.auditing.AuditingConfig;
import com.gg.mafia.global.config.db.TestDbConfig;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class, TestDbConfig.class, AuditingConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
@Transactional
@Slf4j
class FollowingDaoTest {
    @Autowired
    FollowingDao followingDao;

    @Test
    @DisplayName("Followee 조회 - JPA 메서드")
    @Transactional
    public void findByFollowee_Jpa() {
        List<Following> followingList = new ArrayList<>();
        User followee = createUser("followee@naver.com", "123");
        for (int i = 1; i < 100; i++) {
            String email = String.format("followee%d@naver.com", i);
            Following following = createFollowing(createUser(email, "123"), followee);
            followingList.add(following);
        }
        followingDao.saveAll(followingList);
        long startTime = System.currentTimeMillis();
        List<Following> findFollowingList = followingDao.findByFolloweeId(followee.getId());
        long endTime = System.currentTimeMillis();
        log.info("result Time : {}", endTime - startTime);
        Assertions.assertThat(findFollowingList.size()).isEqualTo(99);
    }

    @Test
    @DisplayName("Followee 조회 - QueryDsl")
    @Transactional
    public void findByFollowee_QueryDsl() {
        List<Following> followingList = new ArrayList<>();
        User followee = createUser("followee@naver.com", "123");
        for (int i = 1; i < 100; i++) {
            String email = String.format("followee%d@naver.com", i);
            Following following = createFollowing(createUser(email, "123"), followee);
            followingList.add(following);
        }
        followingDao.saveAll(followingList);
        long startTime = System.currentTimeMillis();
        List<Following> findFollowingList = followingDao.findByFollowee(followee.getId());
        long endTime = System.currentTimeMillis();
        log.info("result Time : {}", endTime - startTime);
        Assertions.assertThat(findFollowingList.size()).isEqualTo(99);
    }

    @Test
    @DisplayName("Follower 조회 - JPA 메서드")
    @Transactional
    public void findByFollower_Jpa() {
        List<Following> followingList = new ArrayList<>();
        User follower = createUser("follower@naver.com", "123");
        for (int i = 1; i < 100; i++) {
            String email = String.format("followee%d@naver.com", i);
            Following following = createFollowing(follower, createUser(email, "123"));
            followingList.add(following);
        }
        followingDao.saveAll(followingList);
        long startTime = System.currentTimeMillis();
        List<Following> findFollowingList = followingDao.findByFollowerId(follower.getId());
        long endTime = System.currentTimeMillis();
        log.info("result Time : {}", endTime - startTime);
        Assertions.assertThat(findFollowingList.size()).isEqualTo(99);
    }

    @Test
    @DisplayName("Follower 조회 - QueryDsl")
    @Transactional
    public void findByFollower_QueryDsl() {
        List<Following> followingList = new ArrayList<>();
        User follower = createUser("follower@naver.com", "123");
        for (int i = 1; i < 100; i++) {
            String email = String.format("followee%d@naver.com", i);
            Following following = createFollowing(follower, createUser(email, "123"));
            followingList.add(following);
        }
        followingDao.saveAll(followingList);
        long startTime = System.currentTimeMillis();
        List<Following> findFollowingList = followingDao.findByFollower(follower.getId());
        long endTime = System.currentTimeMillis();
        log.info("result Time : {}", endTime - startTime);
        Assertions.assertThat(findFollowingList.size()).isEqualTo(99);
    }

    private User createUser(String email, String pw) {
        return User.builder().email(email).password(pw).build();
    }

    private Following createFollowing(User follower, User followee) {
        return Following.builder().follower(follower).followee(followee).build();
    }

}