package com.gg.mafia.sample;


import com.gg.mafia.global.config.network.RestTemplateConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RestTemplateConfig.class})
@Slf4j
public class RestTemplateTest {
    @Autowired
    RestTemplate restTemplate;

    @Test
    public void test() {
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<String> result = restTemplate.exchange("https://google.com", HttpMethod.GET, entity,
                String.class);
        log.debug(result.getBody());
    }
}
