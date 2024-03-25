package com.gg.mafia.global.config.security.oauth;

import com.gg.mafia.domain.member.dto.OAuthUserDto;

public interface OAuthStrategy {
    String GOOGLE = "google";
    String KAKAO = "kakao";
    String NAVER = "naver";

    String getAccessToken(String code);

    OAuthUserDto getOAuthUser(String accessToken);

    String getStrategyCode();
}
