package com.gg.mafia.domain.achievement.domain;

import com.gg.mafia.domain.board.dto.SampleCreateRequest;
import com.gg.mafia.domain.record.domain.JobEnum;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public enum AchievementEnum {
    멍때리기("멍때리기", 1, JobEnum.COMMON, AchievementEnum::common1Logic),
    억울해("억울해", 2, JobEnum.COMMON, AchievementEnum::common2Logic),
    지석진("지석진", 3, JobEnum.COMMON, AchievementEnum::common3Logic),
    럭키가이("럭키가이", 4, JobEnum.COMMON, AchievementEnum::common4Logic),
    불사신("불사신", 4, JobEnum.COMMON, AchievementEnum::common4Logic),
    KILLER("KILLER", 1, JobEnum.MAFIA, AchievementEnum::mafia1Logic),
    진솔한마피아("진솔한 마피아", 2, JobEnum.MAFIA, AchievementEnum::mafia2Logic),
    포커페이스("포커페이스", 3, JobEnum.MAFIA, AchievementEnum::mafia3Logic),
    사냥당한사냥꾼("사냥당한 사냥꾼", 4, JobEnum.MAFIA, AchievementEnum::mafia4Logic),
    궁예("궁예", 1, JobEnum.CITIZEN, AchievementEnum::citizen1Logic),
    부패한시민("부패한 시민", 2, JobEnum.CITIZEN, AchievementEnum::citizen2Logic),
    예비경찰("예비경찰", 3, JobEnum.CITIZEN, AchievementEnum::citizen3Logic),
    해피엔딩("해피엔딩", 4, JobEnum.CITIZEN, AchievementEnum::citizen4Logic),
    순경("순경", 1, JobEnum.POLICE, AchievementEnum::police1Logic),
    정치경찰("정치경찰", 2, JobEnum.POLICE, AchievementEnum::police2Logic),
    정의구현("정의구현", 3, JobEnum.POLICE, AchievementEnum::police3Logic),
    코난("코난", 4, JobEnum.POLICE, AchievementEnum::police4Logic),
    직무유기("직무유기", 1, JobEnum.DOCTOR, AchievementEnum::doctor1Logic),
    돌파이("돌팔이", 2, JobEnum.DOCTOR, AchievementEnum::doctor2Logic),
    이국종("이국종", 3, JobEnum.DOCTOR, AchievementEnum::doctor3Logic),
    살아있는동의보감("살아있는 동의보감", 4, JobEnum.DOCTOR, AchievementEnum::doctor4Logic);

    private static final Map<String, AchievementEnum> valueToName =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(AchievementEnum::getName, Function.identity())));
    //    private static final Map<JobEnum, List<AchievementEnum>> jobEnumMap = Arrays.stream(AchievementEnum.values())
//            .collect(Collectors.groupingBy(AchievementEnum::getJobEnum));
    private final String name;
    private final int step;
    private final JobEnum jobEnum;
    private final Function<SampleCreateRequest, Boolean> logic;

    AchievementEnum(String name, int step, JobEnum jobEnum, Function<SampleCreateRequest, Boolean> logic) {
        this.name = name;
        this.step = step;
        this.jobEnum = jobEnum;
        this.logic = logic;
    }

    public static AchievementEnum getByValue(String value) {
        return valueToName.get(value);
    }

    public boolean execLogic() {
        return this.logic.apply(new SampleCreateRequest());
    }

    // 직업 별 업적 리스트 획득
    public static List<AchievementEnum> getAchievementByJob(JobEnum jobEnum) {
        return Arrays.stream(AchievementEnum.values())
                .filter((e) -> e.getJobEnum().equals(jobEnum)).collect(Collectors.toList());
    }

    // 특정 단계, 직업 별 업적
    public static List<AchievementEnum> getAchievementByStep(JobEnum job, Integer value) {
        return Arrays.stream(AchievementEnum.values())
                .filter(e -> e.jobEnum.getValue() == job.getValue() && e.getStep() == value)
                .collect(Collectors.toList());
    }

    // 각 단계, 직업 별 업적
    public static List<AchievementEnum> getAchievementAllByStepAndJob(AchievementStep steps) {
        List<AchievementEnum> result = new ArrayList<>();
        Map<JobEnum, Integer> map = steps.getStepsToMap();
        map.entrySet().forEach(e -> {
            getAchievementByStep(e.getKey(), e.getValue()).forEach(achievement -> {
                result.add(achievement);
            });
        });
        return result;
    }

    // 완료해야하는 업적
    public static List<AchievementEnum> getAchievementToComplete(List<AchievementEnum> userAchievementList,
                                                                 AchievementStep steps) {
        return getAchievementAllByStepAndJob(steps).stream()
                .filter(e -> !userAchievementList.contains(e))
                .toList();
    }

    // 완료한 업적
    public static List<AchievementEnum> getCompleteAchievement(List<AchievementEnum> achievementsToComplete) {
        return achievementsToComplete.stream().filter(e -> e.execLogic()).toList();
    }

    private static Boolean doctor4Logic(SampleCreateRequest sampleCreateRequest) {
        return true;
    }

    private static Boolean doctor3Logic(SampleCreateRequest sampleCreateRequest) {
        return true;
    }

    private static Boolean doctor2Logic(SampleCreateRequest sampleCreateRequest) {
        return true;
    }

    private static Boolean doctor1Logic(SampleCreateRequest sampleCreateRequest) {
        return true;
    }

    private static Boolean police4Logic(SampleCreateRequest sampleCreateRequest) {
        return true;
    }

    private static Boolean police3Logic(SampleCreateRequest sampleCreateRequest) {
        return true;
    }

    private static Boolean police2Logic(SampleCreateRequest sampleCreateRequest) {
        return true;
    }

    private static Boolean police1Logic(SampleCreateRequest sampleCreateRequest) {
        return true;
    }

    private static Boolean citizen4Logic(SampleCreateRequest sampleCreateRequest) {
        return true;
    }

    private static Boolean citizen3Logic(SampleCreateRequest sampleCreateRequest) {
        return true;
    }

    private static Boolean citizen2Logic(SampleCreateRequest sampleCreateRequest) {
        return true;
    }

    private static Boolean citizen1Logic(SampleCreateRequest sampleCreateRequest) {
        return true;
    }

    private static Boolean mafia4Logic(SampleCreateRequest sampleCreateRequest) {
        return true;
    }

    private static Boolean mafia3Logic(SampleCreateRequest sampleCreateRequest) {
        return true;
    }

    private static Boolean mafia2Logic(SampleCreateRequest sampleCreateRequest) {
        return true;
    }

    public static boolean mafia1Logic(SampleCreateRequest sampleCreateRequest) {
        return true;
    }

    private static Boolean common4Logic(SampleCreateRequest sampleCreateRequest) {
        return true;
    }

    private static Boolean common3Logic(SampleCreateRequest sampleCreateRequest) {
        return true;
    }

    public static boolean common2Logic(SampleCreateRequest sampleCreateRequest) {
        return true;
    }

    private static Boolean common1Logic(SampleCreateRequest sampleCreateRequest) {
        return true;
    }
}
