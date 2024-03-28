package com.gg.mafia.domain.achievement.dao;

import com.gg.mafia.domain.achievement.domain.AchievementStep;
import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.record.domain.JobEnum;
import com.gg.mafia.global.config.AppConfig;
import com.gg.mafia.global.config.auditing.AuditingConfig;
import com.gg.mafia.global.config.db.TestDbConfig;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class, TestDbConfig.class, AuditingConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
@Transactional
@Slf4j
public class AchievementStepDaoTest {
    @Autowired
    AchievementStepDao dao;
    AchievementStep origin;
    AchievementStep find;

    @BeforeEach
    public void setUp() {
        User user = User.builder()
                .email("aaaa@gmail.com")
                .password("test123")
                .build();
        origin = AchievementStep.create(user);
        dao.save(origin);
    }

    @Test
    @DisplayName("findById 값으로 받은 객체의 id값을 비교한다.")
    public void findById_Test() {
        AchievementStep find = dao.findById(origin.getId()).orElseGet(() -> null);
        Assertions.assertThat(find).isEqualTo(origin);
    }

    @Test
    @DisplayName("업적단계 값을 1씩 증가시킨다.")
    public void increaseByCommonAchievement() {
        JobEnum[] arr = new JobEnum[]{JobEnum.DOCTOR, JobEnum.MAFIA};
        repeatByIncreaseTest(arr);
        dao.save(origin);
        AchievementStep find = dao.findById(origin.getId()).get();
        log.info("result : {}", find.getCommonAchieveStep());
        Assertions.assertThat(find).isEqualTo(origin);
    }

    @Test
    @DisplayName("올바르지 않은 이름을 전달하면 IllegalArgument 예외를 발생한다.")
    public void inaccuracyNameThrowIllegalArgument() {
        JobEnum[] arr = new JobEnum[]{JobEnum.DOCTOR, JobEnum.MAFIA, null};
        Assertions.assertThatThrownBy(() -> repeatByIncreaseTest(arr)).isInstanceOf(IllegalArgumentException.class);
    }


    public void increaseTest(JobEnum jobEnum) {
        origin.increase(jobEnum);
    }

    public void repeatByIncreaseTest(JobEnum[] arr) {
        for (int i = 0; i < arr.length; i++) {
            increaseTest(arr[i]);
        }
    }
}
