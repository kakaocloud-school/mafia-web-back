package com.gg.mafia.global.config.security.oauth;

import com.gg.mafia.domain.member.dto.OAuthUserDto;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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

        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(
                env.getProperty("kakao.token-uri", ""),
                request,
                Map.class
        );

        Map<String, Object> response = responseEntity.getBody();

        return (String) response.get("access_token");

    }

    @Override
    public OAuthUserDto getOAuthUser(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(
                env.getProperty("kakao.user-info-uri", ""),
                request,
                Map.class
        );

        Map<String, Object> response = responseEntity.getBody();
        Map<String, Object> kakaoAccount = (Map<String, Object>) response.get("kakao_account");

        return OAuthUserDto.builder()
                .email((String) kakaoAccount.get("email"))
                .strategyCode(getStrategyCode())
                .build();
    }

    @Override
    public String getStrategyCode() {
        return OAuthStrategy.KAKAO;
    }
}
