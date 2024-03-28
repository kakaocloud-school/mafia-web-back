package com.gg.mafia.global.common.aop;

import com.gg.mafia.domain.board.api.SampleApi;
import com.gg.mafia.domain.board.application.SampleService;
import com.gg.mafia.domain.board.dao.SampleDao;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration({"file:src/main/webapp/WEB-INF/root-context.xml",
        "file:src/main/webapp/WEB-INF/servlet-context.xml"})
@Slf4j
public class AopApplyTest {
    @Autowired
    SampleApi api;
    @Autowired
    SampleService service;
    @Autowired
    SampleDao dao;

    @Test
    public void applyTest() {
        log.info("Api Class : {}", api.getClass());
        log.info("Service Class : {}", service.getClass());
        log.info("Dao Class : {}", dao.getClass());

        Assertions.assertThat(AopUtils.isAopProxy(api)).isTrue();
        Assertions.assertThat(AopUtils.isAopProxy(service)).isTrue();
        Assertions.assertThat(AopUtils.isAopProxy(dao)).isTrue();
    }
}
