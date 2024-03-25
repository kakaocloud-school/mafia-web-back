package com.gg.mafia.domain.achievement.dto;

import com.gg.mafia.domain.achievement.domain.AchievementEnum;
import com.gg.mafia.domain.achievement.domain.AchievementStep;
import com.gg.mafia.domain.record.domain.JobEnum;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AchievementDto {
    AchievementStep steps; // 업적 단계
    List<AchievementEnum> userAchievementList; // 보유 업적
    List<AchievementEnum> achievemenToComplete; // clear할 업적
    List<AchievementEnum> completedAchievement; // clear한 업적

    public boolean compareStepsByJob(JobEnum jobEnum) {
        List<AchievementEnum> collect = achievemenToComplete.stream()
                .filter(e -> e.getJobEnum().equals(jobEnum)).collect(Collectors.toList());
        List<AchievementEnum> collect1 = completedAchievement.stream()
                .filter(e -> e.getJobEnum().equals(jobEnum)).collect(Collectors.toList());
        return collect.size() == collect1.size() ? true : false;
    }
}
