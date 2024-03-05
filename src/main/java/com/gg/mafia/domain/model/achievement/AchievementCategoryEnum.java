package com.gg.mafia.domain.model.achievement;

import lombok.Getter;

@Getter
public enum AchievementCategoryEnum {
    MAFIA("마피아"),
    CITIZEN("시민"),
    POLICE("경찰"),
    DOCTOR("의사"),
    COMMON("공통");
    
    private String jobName;

    AchievementCategoryEnum(String jobName) {
        this.jobName = jobName;
    }
}
