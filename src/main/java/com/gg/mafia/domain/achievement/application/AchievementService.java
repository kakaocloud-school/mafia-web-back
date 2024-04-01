package com.gg.mafia.domain.achievement.application;

import com.gg.mafia.domain.achievement.dao.AchievementDao;
import com.gg.mafia.domain.achievement.dao.AchievementStepDao;
import com.gg.mafia.domain.achievement.dao.UserAchievementDao;
import com.gg.mafia.domain.achievement.domain.Achievement;
import com.gg.mafia.domain.achievement.domain.AchievementEnum;
import com.gg.mafia.domain.achievement.domain.AchievementStep;
import com.gg.mafia.domain.achievement.domain.UserAchievement;
import com.gg.mafia.domain.achievement.dto.AchievementDto;
import com.gg.mafia.domain.member.dao.UserDao;
import com.gg.mafia.domain.record.domain.JobEnum;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementService {
    private final AchievementStepDao achievementStepDao;
    private final AchievementDao achievementDao;
    private final UserAchievementDao userAchievementDao;
    private final UserDao userDao;

    // 업적 단계
    public AchievementStep getStepsByUser_id(Long user_id) {
        return achievementStepDao.findByUserId(user_id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("%s에 알맞은 업적단계 데이터가 없습니다.")));
    }

    // 보유 업적
    public List<AchievementEnum> getOwnAchievementByUser_id(Long user_id) {
        return userAchievementDao.findByUserId(user_id).stream()
                .map(e -> e.getAchievement().getAchievementName())
                .toList();
    }

    // 특정 단계 미보유 업적
    public List<AchievementEnum> getAchievementToComplete(List<AchievementEnum> ownAchievement, AchievementStep steps) {
        return AchievementEnum.getAchievementToComplete(ownAchievement,
                steps);
    }

    // 완료한 업적
    public List<AchievementEnum> getCompleteAchievement(List<AchievementEnum> achievementToComplete) {
        return AchievementEnum.getCompleteAchievement(achievementToComplete);
    }

    public AchievementDto getAchievementDto(Long user_id) {
        AchievementStep steps = getStepsByUser_id(user_id);
        List<AchievementEnum> userAchievementList = getOwnAchievementByUser_id(user_id);
        List<AchievementEnum> achievementsToComplete = getAchievementToComplete(userAchievementList,
                steps);
        List<AchievementEnum> completeAchievement = getCompleteAchievement(achievementsToComplete);

        return AchievementDto.builder()
                .steps(steps)
                .userAchievementList(userAchievementList)
                .achievemenToComplete(achievementsToComplete)
                .completedAchievement(completeAchievement)
                .build();
    }

    public void execLogics(Long user_id) {
        AchievementDto dto = getAchievementDto(user_id);
        updateUserAchievement(dto.getCompletedAchievement(), user_id);
        updateSteps(dto);
    }

    public void updateSteps(AchievementDto dto) {
        Arrays.stream(JobEnum.values()).forEach(e -> {
            if (dto.compareStepsByJob(e)) {
                dto.getSteps().increase(e);
            }
        });
        achievementStepDao.save(dto.getSteps());
    }

    public void updateUserAchievement(List<AchievementEnum> completedAchievement, Long user_id) {
        completedAchievement.forEach(e -> {
            UserAchievement obj = UserAchievement.relate(userDao.findById(user_id).get(),
                    achievementDao.findByAchievementName(e).get());
            userAchievementDao.save(obj);
        });
    }

    public List<Achievement> getUserAchievementByUserId(Long userId) {
        List<UserAchievement> userAchievements = userAchievementDao.findByUserId(userId);
        return userAchievements.stream().map(e -> e.getAchievement()).toList();
    }
}
