package com.gg.mafia.domain.achievement.domain;

import com.gg.mafia.domain.record.domain.JobEnum;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum AchievementEnum {
    멍때리기("멍때리기", 1, null),
    억울해("억울해", 2, null),
    지석진("지석진", 3, null),
    럭키가이("럭키가이", 4, null),
    불사신("불사신", 4, null),
    KILLER("KILLER", 1, JobEnum.MAFIA),
    진솔한마피아("진솔한 마피아", 2, JobEnum.MAFIA),
    포커페이스("포커페이스", 3, JobEnum.MAFIA),
    사냥당한사냥꾼("사냥당한 사냥꾼", 4, JobEnum.MAFIA),
    궁예("궁예", 1, JobEnum.CITIZEN),
    부패한시민("부패한 시민", 2, JobEnum.CITIZEN),
    예비경찰("예비경찰", 3, JobEnum.CITIZEN),
    해피엔딩("해피엔딩", 4, JobEnum.CITIZEN),
    순경("순경", 1, JobEnum.POLICE),
    정치경찰("정치경찰", 2, JobEnum.POLICE),
    정의구현("정의구현", 3, JobEnum.POLICE),
    코난("코난", 4, JobEnum.POLICE),
    직무유기("직무유기", 1, JobEnum.DOCTOR),
    돌파이("돌팔이", 2, JobEnum.DOCTOR),
    이국종("이국종", 3, JobEnum.DOCTOR),
    살아있는동의보감("살아있는 동의보감", 4, JobEnum.DOCTOR);

    private static final Map<String, AchievementEnum> valueToName =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(AchievementEnum::getName, Function.identity())));
    private final String name;
    private final int step;
    private final JobEnum jobEnum;

    AchievementEnum(String name, int step, JobEnum jobEnum) {
        this.name = name;
        this.step = step;
        this.jobEnum = jobEnum;
    }


    public static AchievementEnum getByValue(String value) {
        return valueToName.get(value);
    }


}
