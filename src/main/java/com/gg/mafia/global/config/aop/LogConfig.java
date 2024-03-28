package com.gg.mafia.global.config.aop;

import com.gg.mafia.global.config.aop.log.LogTrace;
import com.gg.mafia.global.config.aop.log.LogTraceAspect;
import com.gg.mafia.global.config.aop.log.ThreadLogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogConfig {
    @Bean
    public LogTraceAspect logTraceAspect(LogTrace logTrace) {
        return new LogTraceAspect(logTrace);
    }

    @Bean
    public LogTrace logTrace() {
        return new ThreadLogTrace();
    }
}
