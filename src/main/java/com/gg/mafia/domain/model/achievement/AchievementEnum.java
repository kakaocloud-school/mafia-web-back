package com.gg.mafia.domain.model.achievement;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum AchievementEnum {
    멍때리기("멍때리기", 1, AchievementCategoryEnum.COMMON),
    억울해("억울해", 2, AchievementCategoryEnum.COMMON),
    지석진("지석진", 3, AchievementCategoryEnum.COMMON),
    럭키가이("럭키가이", 4, AchievementCategoryEnum.COMMON),
    불사신("불사신", 4, AchievementCategoryEnum.COMMON),
    KILLER("KILLER", 1, AchievementCategoryEnum.MAFIA),
    진솔한마피아("진솔한 마피아", 2, AchievementCategoryEnum.MAFIA),
    포커페이스("포커페이스", 3, AchievementCategoryEnum.MAFIA),
    사냥당한사냥꾼("사냥당한 사냥꾼", 4, AchievementCategoryEnum.MAFIA),
    궁예("궁예", 1, AchievementCategoryEnum.CITIZEN),
    부패한시민("부패한 시민", 2, AchievementCategoryEnum.CITIZEN),
    예비경찰("예비경찰", 3, AchievementCategoryEnum.CITIZEN),
    해피엔딩("해피엔딩", 4, AchievementCategoryEnum.CITIZEN),
    순경("순경", 1, AchievementCategoryEnum.POLICE),
    정치경찰("정치경찰", 2, AchievementCategoryEnum.POLICE),
    정의구현("정의구현", 3, AchievementCategoryEnum.POLICE),
    코난("코난", 4, AchievementCategoryEnum.POLICE),
    직무유기("직무유기", 1, AchievementCategoryEnum.DOCTOR),
    돌파이("돌팔이", 2, AchievementCategoryEnum.DOCTOR),
    이국종("이국종", 3, AchievementCategoryEnum.DOCTOR),
    살아있는동의보감("살아있는 동의보감", 4, AchievementCategoryEnum.DOCTOR);

    private static final Map<Integer, AchievementEnum> valueToName =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(AchievementEnum::getStep, Function.identity())));
    private final String name;
    private final int step;
    private final AchievementCategoryEnum category;

    AchievementEnum(String name, int step, AchievementCategoryEnum category) {
        this.name = name;
        this.step = step;
        this.category = category;
    }


    public static AchievementEnum getByValue(int value) {
        return valueToName.get(value);
    }


}
