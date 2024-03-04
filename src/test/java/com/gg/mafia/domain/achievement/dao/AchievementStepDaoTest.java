package com.gg.mafia.domain.achievement.dao;

import com.gg.mafia.domain.achievement.domain.AchievementStep;
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
@ContextConfiguration(value = "file:src/main/webapp/WEB-INF/root-context.xml")
@Transactional
@Slf4j
public class AchievementStepDaoTest {
    @Autowired
    AchievementStepDao dao;
    AchievementStep origin;
    AchievementStep find;

    @BeforeEach
    public void setUp() {
        origin = AchievementStep.create();
        dao.save(origin);
    }

    @Test
    @DisplayName("findById 값으로 받은 객체의 id값을 비교한다.")
    public void findById_Test() {
        AchievementStep find = dao.findById(origin.getId()).get();
        Assertions.assertThat(find.getId()).isEqualTo(origin.getId());
    }

    @Test
    @DisplayName("업적단계 값을 1씩 증가시킨다.")
    public void increaseByCommonAchievement() {
        String[] names = new String[]{"common", "mafia", "police", "citizen", "doctor"};
        repeatByIncreaseTest(names);
        dao.save(origin);
        AchievementStep find = dao.findById(origin.getId()).get();
        log.info("result : {}", find.getCommonAchieveStep());
        Assertions.assertThat(find).isEqualTo(origin);
    }

    @Test
    @DisplayName("올바르지 않은 이름을 전달하면 IllegalArgument 예외를 발생한다.")
    public void inaccuracyNameThrowIllegalArgument() {
        String[] names = new String[]{"common", "mafia", "police", "citizen", "couple"};
        Assertions.assertThatThrownBy(() -> repeatByIncreaseTest(names)).isInstanceOf(IllegalArgumentException.class);
    }


    public void increaseTest(String name) {
        origin.increase(name);
    }

    public void repeatByIncreaseTest(String[] names) {
        for (int i = 0; i < names.length; i++) {
            increaseTest(names[i]);
        }
    }
}
