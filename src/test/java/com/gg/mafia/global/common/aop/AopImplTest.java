package com.gg.mafia.global.common.aop;

import com.gg.mafia.global.common.aop.code.TestService;
import com.gg.mafia.global.config.AppConfig;
import com.gg.mafia.global.config.aop.TestLogConfig;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class, TestLogConfig.class})
@Slf4j
public class AopImplTest {
    @Autowired
    TestService testService;

    @Test
    public void test() {
        testService.logic();
        Assertions.assertThat(AopUtils.isAopProxy(testService)).isTrue();
    }
}
