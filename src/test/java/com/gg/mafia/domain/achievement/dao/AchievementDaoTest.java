package com.gg.mafia.domain.achievement.dao;

import com.gg.mafia.domain.achievement.domain.Achievement;
import com.gg.mafia.domain.achievement.domain.AchievementEnum;
import com.gg.mafia.global.config.AppConfig;
import com.gg.mafia.global.config.auditing.AuditingConfig;
import com.gg.mafia.global.config.db.TestDbConfig;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class, TestDbConfig.class, AuditingConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
@Transactional
@Slf4j
public class AchievementDaoTest {
    @Autowired
    AchievementDao dao;

    @BeforeEach
    public void setUp() {
        insertAchievements();
    }

    @Test
    @DisplayName("기본 업적 개수를 확인한다.")
    public void dbConfigTest() {
        Assertions.assertThat(AchievementEnum.values().length).isEqualTo((long) dao.getAchievementCount());
    }

    @Test
    @DisplayName("achieveName값으로 조회한다.")
    public void findByValue_Test() {
        insertAchievements();
        Assertions.assertThat(dao.findByAchieveName(AchievementEnum.궁예).get().getAchievementName())
                .isEqualTo(AchievementEnum.궁예);
    }

    private void insertAchievements() {
        Arrays.stream(AchievementEnum.values()).forEach(this::insertAchievement);
    }

    private void insertAchievement(AchievementEnum value) {
        if (dao.findByAchieveName(value).isEmpty()) {
            trySave(dao, Achievement.builder()
                    .achievementName(value)
                    .jobName(value.getJobEnum())
                    .build());
        }
    }

    private <T> void trySave(JpaRepository<T, Long> dao, T entity) {
        try {
            dao.save(entity);
        } catch (Exception e) {
            return;
        }
    }
}
