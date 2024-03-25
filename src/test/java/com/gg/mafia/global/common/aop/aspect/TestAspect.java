package com.gg.mafia.global.common.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Slf4j
public class TestAspect {
    @Around("execution(* com.gg.mafia.global.common.aop.code.TestService.*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("로그 호출");
        Object result = joinPoint.proceed();
        log.info("로그 종료");
        return result;
    }
}
