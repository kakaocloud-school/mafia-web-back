package com.gg.mafia.global.config.aop;

import com.gg.mafia.global.common.aop.aspect.TestAspect;
import com.gg.mafia.global.common.aop.code.TestService;
import org.springframework.context.annotation.Bean;

public class TestLogConfig {
    @Bean
    public TestAspect testAspect() {
        return new TestAspect();
    }

    @Bean
    public TestService testService() {
        return new TestService();
    }
}
