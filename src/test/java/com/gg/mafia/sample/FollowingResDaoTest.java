package com.gg.mafia.sample;

import com.gg.mafia.domain.member.dao.FollowingDao;
import com.gg.mafia.domain.member.dao.FollowingResDao;
import com.gg.mafia.domain.member.dao.UserDao;
import com.gg.mafia.domain.member.domain.Following;
import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.member.dto.FollowingResponse;
import com.gg.mafia.domain.profile.domain.Profile;
import com.gg.mafia.global.config.AppConfig;
import com.gg.mafia.global.config.auditing.AuditingConfig;
import com.gg.mafia.global.config.db.TestDbConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class, TestDbConfig.class, AuditingConfig.class})
@Transactional
@Slf4j
public class FollowingResDaoTest {
    private final Random rand;
    @Autowired
    FollowingDao followingDao;
    @Autowired
    FollowingResDao followingResDao;
    @Autowired
    UserDao userDao;

    User user;
    Profile profile;
    List<User> users = new ArrayList<>();

    public FollowingResDaoTest() {
        this.rand = new Random();
    }

    @BeforeEach
    public void beforeEach() {
        for (int i = 1; i < 11; i++) {
            user = createUser(String.format("TEST_USER%d@naver.com", i), "123");
            saveToUser(user);
        }
        users = userDao.findAll();
    }

    @AfterEach
    public void afterEach() {

    }

    @Test
    public void test() {
        for (int i = 1; i < users.size(); i++) {
            Following following = Following.relate(users.get(0), users.get(i));
            followingDao.save(following);
        }
        PageRequest pageRequest = PageRequest.of(1, 4);
        Page<FollowingResponse> result = followingResDao.findByFollowerId(users.get(0).getId(), pageRequest);
        result.stream().forEach(e -> log.info("obj : {}", e));


    }

    private void saveToUser(User user) {
        profile = createProfile(user);
        user.setProfile(profile);
        userDao.save(user);
    }

    private List<Following> createRandomFollowingsByFollower(User follower, int count) {
        List<Following> randomFollowingList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User randomFollowee = createUser(String.format("Followee%d@naver.com", i), "123");
            randomFollowingList.add(createFollowing(follower, randomFollowee));
        }
        return randomFollowingList;
    }

    private List<Following> createRandomFollowingsByFollowee(User followee, int count) {
        List<Following> randomFollowingList = new ArrayList<>();
        for (int i = 1; i < count; i++) {
            String email = String.format("followee%d@naver.com", i);
            Following following = createFollowing(createUser(email, "123"), followee);
            randomFollowingList.add(following);
        }
        return randomFollowingList;
    }

    private Following createFollowing(User follower, User followee) {
        return Following.builder().follower(follower).followee(followee).build();
    }

    private User createUser(String email, String pw) {
        return User.builder().email(email).password(pw).build();
    }

    private Profile createProfile(User user) {
        return Profile.builder().user(user).userName(user.getEmail() + "닉네임").build();
    }
}
