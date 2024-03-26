package com.gg.mafia.domain.member.domain;

import com.gg.mafia.global.config.AppConfig;
import com.gg.mafia.global.config.auditing.AuditingConfig;
import com.gg.mafia.global.config.db.TestDbConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class, TestDbConfig.class, AuditingConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
@DisplayName("사용자 팔로우 테스트")
@Slf4j
class FollowingTest {
    private final Random rand;
    @PersistenceUnit
    private EntityManagerFactory emf;
    private EntityManager em;
    private Following following;
    private List<Following> followingList;
    private User follower1;
    private User follower2;
    private User followee;

    public FollowingTest() {
        this.rand = new Random();
    }

    @BeforeEach
    public void beforeAll() {
        em = emf.createEntityManager();
        em.getTransaction().begin();

        follower1 = createUser("follower1@naver.com", "123");
        follower2 = createUser("follower2@naver.com", "123");
        followee = createUser("followee@naver.com", "123");

        em.persist(follower1);
        em.persist(follower2);
        em.persist(followee);
    }

    @AfterEach
    public void afterEach() {
        em.getTransaction().rollback();
        em.clear();
    }

    @Test
    @DisplayName("Following 생성 테스트")
    public void createFollowing() {
        Following following = createFollowing(follower1, followee);

        em.persist(following);
        Following findFollowing = em.find(Following.class, following.getId());

        Assertions.assertThat(findFollowing).isEqualTo(following);
    }

    @Test
    @DisplayName("Following 삭제 테스트")
    public void removeFollowing() {
        int count = rand.nextInt(1, 5);
        List<Following> randomFollowingList = new ArrayList<>();
        for (int i = 1; i < count; i++) {
            User randomUser = createUser(UUID.randomUUID().toString().substring(0, 5), "123");

            Following follow = createFollowing(follower1, randomUser);
            randomFollowingList.add(follow);

            em.persist(follow);
            em.flush();
            log.info("[create] {} - {}", follower1.getEmail(), randomUser.getEmail());
        }
        for (int i = 0; i < randomFollowingList.size(); i++) {
            em.createQuery("delete from Following f where f.follower =:follower and f.followee =:followee")
                    .setParameter("follower", follower1)
                    .setParameter("followee", randomFollowingList.get(i).getFollowee())
                    .executeUpdate();
            log.info("[remove] {} - {}", follower1.getEmail(), randomFollowingList.get(i).getFollowee().getEmail());
        }
        int countFromDb = em.createQuery("select f from Following f where follower =:follower")
                .setParameter("follower", follower1)
                .getFirstResult();
        Assertions.assertThat(countFromDb).isEqualTo(0);
    }

    @Test
    @DisplayName("Following 조회 테스트")
    public void selectFollowing() {
        int count = rand.nextInt(1, 10);
        int target1_follower = 0;
        int target2_follower = 0;
        for (int i = 1; i < count; i++) {
            User randomUser = createUser(UUID.randomUUID().toString().substring(0, 5), "123");
            if (count % 2 != 0) {
                target1_follower++;
                Following follow = createFollowing(randomUser, follower1);
                em.persist(follow);
            } else {
                target2_follower++;
                Following follow = createFollowing(randomUser, follower2);
                em.persist(follow);
            }
        }
        List<Following> target1FollowingList = em.createQuery("SELECT f FROM Following f where f.followee =:followee")
                .setParameter("followee", follower1)
                .getResultList();
        Assertions.assertThat(target1FollowingList.size())
                .isEqualTo(target1_follower);
    }

    private User createUser(String email, String pw) {
        return User.builder().email(email).password(pw).build();
    }

    private Following createFollowing(User follower, User followee) {
        return Following.builder().follower(follower).followee(followee).build();
    }
}