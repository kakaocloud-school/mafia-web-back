package com.gg.mafia.domain.achievement.domain;

import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.record.domain.JobEnum;
import com.gg.mafia.global.config.AppConfig;
import com.gg.mafia.global.config.auditing.AuditingConfig;
import com.gg.mafia.global.config.db.TestDbConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;
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
@Slf4j
public class AchievementTest {
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
    @DisplayName("UserAchievement를 저장하면 User와 Achivement가 저장된다.")
    public void saveCascade() {
        UserAchievement userAchievement = UserAchievement.builder()
                .user(createUser())
                .achievement(createAchievement())
                .build();
        em.persist(userAchievement);
        em.flush();
        String userJpql = String.format("select u from User u where u.email = 'TEST_USER'");
        User findUser = em.createQuery(userJpql, User.class).getSingleResult();
        Assertions.assertThat(userAchievement.getUser()).isEqualTo(findUser);

        String achievementJpql = String.format("select a from Achievement a where a.achievementName = '%s'",
                userAchievement.getAchievement().getAchievementName());
        Achievement findAchievement = em.createQuery(achievementJpql, Achievement.class)
                .getSingleResult();
        Assertions.assertThat(findAchievement).isEqualTo(userAchievement.getAchievement());
    }

    @Test
    @DisplayName("UserAchievement를 저장한다.")
    public void makeUserAchievement() {
        UserAchievement userAchievement = UserAchievement.builder()
                .user(createUser())
                .achievement(createAchievement())
                .build();
        em.persist(userAchievement);
        em.flush();
        UserAchievement findUserAchievement = em.find(UserAchievement.class, userAchievement.getId());
        Assertions.assertThat(findUserAchievement).isEqualTo(userAchievement);
    }


    public User createUser() {
        User user = User.builder()
                .email("TEST_USER")
                .password("123")
                .build();
        return user;
    }

    public Achievement createAchievement() {
        return Achievement.builder()
                .achievementName(AchievementEnum.KILLER)
                .jobName(JobEnum.MAFIA)
                .build();
    }
}
