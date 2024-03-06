package com.gg.mafia.domain.achievement.dao;

import com.gg.mafia.domain.achievement.domain.Achievement;
import com.gg.mafia.domain.achievement.domain.AchievementStep;
import com.gg.mafia.domain.achievement.domain.UserAchievement;
import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.global.config.AppConfig;
import com.gg.mafia.global.config.auditing.AuditingConfig;
import com.gg.mafia.global.config.db.TestDbConfig;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class, TestDbConfig.class, AuditingConfig.class})
@Transactional
@Slf4j
class UserAchievementDaoTest {
    @Autowired
    UserAchievementDao dao;
    UserAchievement origin;
    UserAchievement find;

    @BeforeEach
    public void setUp() {
        origin = UserAchievement.builder()
                .user(createUser())
                .achievement(createAchievement())
                .build();
        dao.save(origin);
    }

    @Test
    @DisplayName("findById 값으로 받은 객체의 id값을 비교한다.")
    public void findById_Test() {
        UserAchievement find = dao.findById(origin.getId()).get();
        Assertions.assertThat(find.getId()).isEqualTo(origin.getId());
    }

    @Test
    @DisplayName("findByUser_id 값으로 받은 객체의 user_id 값을 비교한다.")
    public void findByUser_id_Test() {
        find = dao.findByUser_id(origin.getUser().getId()).get();
        Assertions.assertThat(find.getUser().getId()).isEqualTo(origin.getUser().getId());
    }

    @Test
    @DisplayName("findByAchievement_id 값으로 받은 객체의 achievement_id 값을 비교한다.")
    public void findByAchievement_id_Test() {
        find = dao.findByAchievement_id(origin.getAchievement().getId()).get();
        Assertions.assertThat(find.getAchievement().getId()).isEqualTo(origin.getAchievement().getId());
    }

    public User createUser() {
        User user = User.builder()
                .email("TEST_USER")
                .password("123")
                .build();
        AchievementStep.create(user);
        return user;
    }

    public Achievement createAchievement() {
        Random rnd = new Random();
        return Achievement.builder()
                .name("TEST_ACHIEVEMENT")
                .step(rnd.nextInt(4) + 1)
                .build();
    }

}