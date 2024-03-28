package com.gg.mafia.domain.member.application;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.gg.mafia.domain.member.dao.FollowingDao;
import com.gg.mafia.domain.member.dao.UserDao;
import com.gg.mafia.domain.member.domain.Following;
import com.gg.mafia.domain.member.domain.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("팔로우 서비스 테스트")
@Slf4j
class FollowingServiceTest {
    @InjectMocks
    FollowingService service;
    @Mock
    UserDao userDao;
    @Mock
    FollowingDao followingDao;
    User user;

    @BeforeEach
    public void setUp() {
        user = Mockito.spy(User.builder().email("TEST@naver.com").password("123").build());
    }

    @Test
    @DisplayName("팔로우 저장 기능 테스트")
    public void insertFollowing_Test() {
        // Given
        User follower = createUser("TEST_FOLLOWER@naver.com", "123");
        User followee = createUser("TEST_FOLLOWEE@naver.com", "123");
        Following following = createFollowing(follower, followee);

        given(userDao.findByEmail(follower.getEmail())).willReturn(Optional.of(follower));
        given(userDao.findByEmail(followee.getEmail())).willReturn(Optional.of(followee));
        given(followingDao.save(following)).willReturn(following);
        // When
        Following saveFollowing = service.insertFollowing(follower.getEmail(), followee.getEmail());

        Assertions.assertThat(saveFollowing.getFollower().getEmail()).isEqualTo(follower.getEmail());
        Assertions.assertThat(saveFollowing.getFollowee().getEmail()).isEqualTo(followee.getEmail());

        // Then
        then(userDao).should().findByEmail(follower.getEmail());
        then(userDao).should().findByEmail(followee.getEmail());
        then(followingDao).should().save(following);
    }

    @Test
    @DisplayName("특정 사용자가 팔로우한 Following 리스트 조회 테스트")
    public void getFollowees_Test() {
        // Given
        User follower = createUser("TEST_FOLLOWER@naver.com", "123");

        List<Following> followings = createFollowingsFromFollower(follower, 50)
                .stream().filter(e -> e.getFollower().getEmail().equals(follower.getEmail())).toList();

        given(followingDao.findByFollowerId(follower.getId())).willReturn(followings);
        given(userDao.findByEmail(follower.getEmail())).willReturn(Optional.of(follower));

        // When
        List<Following> result = service.getFollowees(follower.getEmail());
        Assertions.assertThat(result.size()).isEqualTo(followings.size());
        // Then
        then(userDao).should().findByEmail(follower.getEmail());
        then(followingDao).should().findByFollowerId(follower.getId());
    }

    @Test
    @DisplayName("특정 사용자를 팔로우한 Following 리스트 조회 테스트 ")
    public void getFollowers_test() {
        // Given
        User followee = createUser("TEST_FOLLOWEE@naver.com", "123");

        List<Following> followings = createFollowingsFromFollowee(followee, 50)
                .stream().filter(e -> e.getFollowee().getEmail().equals(followee.getEmail())).toList();

        given(followingDao.findByFolloweeId(followee.getId())).willReturn(followings);
        given(userDao.findByEmail(followee.getEmail())).willReturn(Optional.of(followee));

        // When
        List<Following> result = service.getFollowers(followee.getEmail());
        log.info("result size : {}", result.size());
        log.info("followings size : {}", followings.size());
        Assertions.assertThat(result.size()).isEqualTo(followings.size());

        // Then
        then(userDao).should().findByEmail(followee.getEmail());
        then(followingDao).should().findByFolloweeId(followee.getId());
    }

    @Test
    @DisplayName("특정 Followee 삭제 테스트")
    public void removeFollowee_Test() {
        // Given
        User follower = Mockito.spy(createUser("TEST_FOLLOWER@naver.com", "123"));
        User followee = Mockito.spy(createUser("TEST_FOLLOWEE@naver.com", "123"));
        Following following = createFollowing(follower, followee);
        List<Following> followings = createFollowingsFromFollower(follower, 50);
        followings.add(following);

        given(follower.getId()).willReturn(1L);
        given(followee.getId()).willReturn(2L);
        given(userDao.findByEmail(follower.getEmail())).willReturn(Optional.of(follower));
        given(userDao.findByEmail(followee.getEmail())).willReturn(Optional.of(followee));
        given(followingDao.findByFollowerIdAndFolloweeId(follower.getId(), followee.getId()))
                .willReturn(Optional.of(following));
        // When
        service.removeFollowee(follower.getEmail(), followee.getEmail());

        // Then
        then(userDao).should().findByEmail(follower.getEmail());
        then(userDao).should().findByEmail(followee.getEmail());
        then(followingDao).should().findByFollowerIdAndFolloweeId(follower.getId(), followee.getId());
        then(followingDao).should().delete(following);
    }

    private List<Following> createFollowings(int count) {
        Random rand = new Random();
        List<Following> followings = new ArrayList<>();
        for (int i = 1; i < count; i++) {
            User randomFollower = createUser(UUID.randomUUID().toString().substring(0, 5), "123");
            User randomFollowee = createUser(UUID.randomUUID().toString().substring(0, 5), "123");
            followings.add(createFollowing(randomFollower, randomFollowee));
        }
        return followings;
    }

    private List<Following> createFollowingsFromFollowee(User followee, int count) {
        Random rand = new Random();
        List<Following> followings = new ArrayList<>();
        for (int i = 1; i < count; i++) {
            User randomFollower = createUser(UUID.randomUUID().toString().substring(0, 5), "123");
            if (rand.nextInt(1, 10) > 5) {
                followings.add(createFollowing(randomFollower, followee));
            } else {
                User randomFollowee = createUser(UUID.randomUUID().toString().substring(0, 5), "123");
                followings.add(createFollowing(randomFollower, randomFollowee));
            }
        }
        return followings;
    }

    private List<Following> createFollowingsFromFollower(User follower, int count) {
        Random rand = new Random();
        List<Following> followings = new ArrayList<>();
        for (int i = 1; i < count; i++) {
            User randomFollowee = createUser(UUID.randomUUID().toString().substring(0, 5), "123");
            if (rand.nextInt(1, 10) > 5) {
                followings.add(createFollowing(follower, randomFollowee));
            } else {
                User randomFollower = createUser(UUID.randomUUID().toString().substring(0, 5), "123");
                followings.add(createFollowing(randomFollower, randomFollowee));
            }
        }
        return followings;
    }

    private User createUser(String email, String pw) {
        return User.builder().email(email).password(pw).build();
    }


    private Following createFollowing(User follower, User followee) {
        return Following.builder().follower(follower).followee(followee).build();
    }
}