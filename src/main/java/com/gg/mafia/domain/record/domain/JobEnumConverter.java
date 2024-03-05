package com.gg.mafia.domain.record.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class JobEnumConverter implements AttributeConverter<JobEnum, Integer> {
    @Override
    public Integer convertToDatabaseColumn(JobEnum jobEnum) {
        if (jobEnum == null) {
            return null;
        }
        return jobEnum.getValue();
    }

    @Override
    public JobEnum convertToEntityAttribute(Integer value) {
        if (value == null) {
            return null;
        }
        return JobEnum.getByValue(value);
    }
}
