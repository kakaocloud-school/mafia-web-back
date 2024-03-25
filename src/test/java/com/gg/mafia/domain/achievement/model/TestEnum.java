package com.gg.mafia.domain.achievement.model;

public enum TestEnum {
    USER(0),
    ADMIN(1);

    private int value;

    TestEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
