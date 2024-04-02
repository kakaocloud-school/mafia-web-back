package com.gg.mafia.domain.member.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.gg.mafia.domain.member.dao.FollowingDao;
import com.gg.mafia.domain.member.dao.FollowingResDao;
import com.gg.mafia.domain.member.dao.UserDao;
import com.gg.mafia.domain.member.domain.Following;
import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.member.dto.FollowingResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
    @Mock
    FollowingResDao followingResDao;
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
        given(userDao.findById(1L)).willReturn(Optional.of(followee));
        given(followingDao.save(following)).willReturn(following);
        // When
        Following saveFollowing = service.insertFollowing(follower.getEmail(), 1L);

        // Then
        then(followingDao).should().save(following);
    }

    @Test
    @DisplayName("특정 사용자가 팔로우한 Following 리스트 조회 테스트")
    public void getFollowees_Test() {
        // Given
        User follower = Mockito.spy(createUser("TEST_FOLLOWER@naver.com", "123"));

        List<Following> origin = createFollowingsFromFollower(follower, 50);
        List<FollowingResponse> filter = origin.stream()
                .filter(e -> e.getFollower().getEmail().equals(follower.getEmail()))
                .map(e -> new FollowingResponse())
                .toList();

        Pageable pageable = PageRequest.of(0, 4);
        given(follower.getId()).willReturn(1L);
        given(userDao.findById(1L)).willReturn(Optional.of(follower));
        given(followingResDao.findByFollowerId(follower.getId(), pageable)).willReturn(
                new PageImpl<FollowingResponse>(filter, pageable, origin.size()));

        // When
        service.getFollowees(1L, pageable);
        // Then
        then(followingResDao).should().findByFollowerId(1L, pageable);
    }

    @Test
    @DisplayName("특정 사용자를 팔로우한 Following 리스트 조회 테스트 ")
    public void getFollowers_test() {
        // Given
        User followee = Mockito.spy(createUser("TEST_FOLLOWEE@naver.com", "123"));
        List<Following> origin = createFollowingsFromFollowee(followee, 50);
        List<FollowingResponse> filter = origin.stream()
                .filter(e -> e.getFollowee().getEmail().equals(followee.getEmail()))
                .map(e -> new FollowingResponse())
                .toList();

        Pageable pageable = PageRequest.of(0, 4);
        given(followee.getId()).willReturn(1L);
        given(userDao.findById(1L)).willReturn(Optional.of(followee));
        given(followingResDao.findByFolloweeId(followee.getId(), pageable)).willReturn(
                new PageImpl<FollowingResponse>(filter, pageable, origin.size()));

        // When
        service.getFollowers(1L, pageable);
        // Then
        then(followingResDao).should().findByFolloweeId(1L, pageable);
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
        given(userDao.findById(followee.getId())).willReturn(Optional.of(followee));
        given(followingDao.findByFollowerIdAndFolloweeId(follower.getId(), followee.getId()))
                .willReturn(Optional.of(following));
        // When
        service.removeFollowee(follower.getEmail(), 2L);

        // Then
        then(followingDao).should().delete(any());
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