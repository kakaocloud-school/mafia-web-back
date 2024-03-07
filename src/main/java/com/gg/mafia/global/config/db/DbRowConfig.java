package com.gg.mafia.global.config.db;

import com.gg.mafia.domain.achievement.dao.AchievementDao;
import com.gg.mafia.domain.achievement.domain.Achievement;
import com.gg.mafia.domain.achievement.domain.AchievementEnum;
import com.gg.mafia.domain.member.dao.RoleDao;
import com.gg.mafia.domain.member.domain.Role;
import com.gg.mafia.domain.model.RoleEnum;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

@Configuration
@RequiredArgsConstructor
public class DbRowConfig implements InitializingBean {
    private final RoleDao roleDao;
    private final AchievementDao achievementDao;

    @Override
    public void afterPropertiesSet() throws Exception {
        insertRoles();
        insertAchievements();
    }

    private void insertRoles() {
        Arrays.stream(RoleEnum.values()).forEach(this::insertRole);
    }

    private void insertRole(RoleEnum value) {
        if (roleDao.findByValue(value).isEmpty()) {
            trySave(roleDao, Role.builder()
                    .value(value)
                    .build());
        }
    }

    private void insertAchievements() {
        Arrays.stream(AchievementEnum.values()).forEach(this::insertAchievement);
    }

    private void insertAchievement(AchievementEnum value) {
        if (achievementDao.findByValue(value).isEmpty()) {
            trySave(achievementDao, Achievement.builder()
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
