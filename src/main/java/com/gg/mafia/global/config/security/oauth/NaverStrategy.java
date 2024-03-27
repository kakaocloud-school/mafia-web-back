package com.gg.mafia.global.config.security.oauth;

import com.gg.mafia.domain.member.dto.OAuthUserDto;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class NaverStrategy implements OAuthStrategy{
    private final Environment env;
    private final RestTemplate restTemplate;

    @Override
    public String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("client_id", env.getProperty("naver.client-id"));
        requestBody.add("client_secret", env.getProperty("naver.client-secret"));
        requestBody.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(
                env.getProperty("naver.token-uri", ""),
                request,
                Map.class
        );

        Map<String, Object> response = responseEntity.getBody();

        return (String) response.get("access_token");
    }

    @Override
    public OAuthUserDto getOAuthUser(String accessToken) {
        String url = "%s?access_token=%s".formatted(env.getProperty("naver.user-info-uri"), accessToken);
        Map response = restTemplate.getForObject(url, Map.class);
        Map<String, Object> responseMap = (Map<String, Object>) response.get("response");
        return OAuthUserDto.builder()
                .email((String) responseMap.get("email"))
                .strategyCode(getStrategyCode())
                .build();
    }

    @Override
    public String getStrategyCode() {
        return OAuthStrategy.NAVER;
    }
}
