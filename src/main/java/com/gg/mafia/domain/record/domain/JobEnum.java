package com.gg.mafia.domain.record.domain;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum JobEnum {
    CITIZEN(0),
    MAFIA(1),
    DOCTOR(2),
    POLICE(3),
    COMMON(4);

    private static final Map<Integer, JobEnum> valueToName =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(JobEnum::getValue, Function.identity())));
    private final int value;

    JobEnum(int value) {
        this.value = value;
    }

    public static JobEnum getByValue(int value) {
        return valueToName.get(value);
    }
}
