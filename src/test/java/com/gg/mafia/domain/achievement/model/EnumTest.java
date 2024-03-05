package com.gg.mafia.domain.achievement.model;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class EnumTest {

    @Test
    public void test() {
        TestEnum userEnum = TestEnum.USER;
        log.info("userEnum get Value {} ", userEnum.getValue());
        TestEnum adminEnum = TestEnum.ADMIN;
        log.info("adminEnum get Value {} ", adminEnum.getValue());
    }
}
