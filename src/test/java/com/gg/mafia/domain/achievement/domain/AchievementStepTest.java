package com.gg.mafia.domain.achievement.domain;

import com.gg.mafia.domain.member.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(value = "file:src/main/webapp/WEB-INF/root-context.xml")
@Slf4j
public class AchievementStepTest {
    @PersistenceUnit
    EntityManagerFactory emf;
    EntityManager em;
    EntityTransaction tx;

    @BeforeEach
    public void setUp() {
        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();
    }

    @AfterEach
    public void cleanUp() {
        tx.rollback();
        em.clear();
        em.close();
    }

    @Test
    @DisplayName("AchievementStep 저장를 저장한다. ")
    public void createAchievementStep_Test() {
        AchievementStep achievementStep = AchievementStep.create();
        em.persist(achievementStep);
        em.flush();
        AchievementStep findAchievementStep = em.find(AchievementStep.class, achievementStep.getId());
        Assertions.assertThat(findAchievementStep).isEqualTo(achievementStep);
    }

    @Test
    @DisplayName("User를 저장하면 AchievementStep도 함께 저장된다.")
    public void saveCascade_AchievementStep_Test() {
        AchievementStep achievementStep = AchievementStep.create();
        User user = createUser("TEST_USER", "123", achievementStep);
        em.persist(user);
        em.flush();
        User findUser = em.find(User.class, user.getId());
        Assertions.assertThat(findUser.getAchievementStep()).isEqualTo(achievementStep);
    }

    public User createUser(String email, String pw, AchievementStep achievementStep) {
        return User.builder()
                .email(email)
                .password(pw)
                .achievementStep(achievementStep)
                .build();
    }
}
