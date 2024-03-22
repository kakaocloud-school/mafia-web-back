package com.gg.mafia.global.config.security.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gg.mafia.domain.member.dto.OAuthUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoStrategy implements OAuthStrategy{
    private final Environment env;
    private final RestTemplate restTemplate;
    @Override
    public String getAccessToken(String code) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded; charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", env.getProperty("kakao.client-id"));
        body.add("redirect_uri",  env.getProperty("kakao.redirect-uri"));
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        String response = restTemplate.exchange(env.getProperty("kakao.token-uri"), HttpMethod.POST, request, String.class).getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = objectMapper.readTree(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return rootNode.path("access_token").asText();

    }

    @Override
    public OAuthUserDto getOAuthUser(String accessToken) {
        String userInfoUrl = env.getProperty("kakao.user-info-uri");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        String response = restTemplate.exchange(userInfoUrl, HttpMethod.POST, request, String.class).getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = objectMapper.readTree(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return OAuthUserDto.builder()
                .email(rootNode.path("email").asText())
                .strategyCode(getStrategyCode())
                .build();
    }

    @Override
    public String getStrategyCode() {
        return OAuthStrategy.KAKAO;
    }
}
