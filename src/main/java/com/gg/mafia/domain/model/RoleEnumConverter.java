package com.gg.mafia.domain.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RoleEnumConverter implements AttributeConverter<RoleEnum, Integer> {
    @Override
    public Integer convertToDatabaseColumn(RoleEnum roleEnum) {
        return roleEnum.getValue();
    }

    @Override
    public RoleEnum convertToEntityAttribute(Integer value) {
        return RoleEnum.getByValue(value);
    }
}
