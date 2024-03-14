package com.gg.mafia.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySources({
        @PropertySource("classpath:config/db.properties"),
        @PropertySource("classpath:config/application.properties"),
        @PropertySource("classpath:config/swagger.properties"),
        @PropertySource("classpath:config/security.properties")
})
@EnableAspectJAutoProxy
public class AppConfig {
    @Bean
    public PropertySourcesPlaceholderConfigurer dataSource() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
