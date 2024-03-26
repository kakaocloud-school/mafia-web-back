package com.gg.mafia.domain.member.application;

import com.gg.mafia.domain.member.dao.UserDao;
import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.member.dto.OAuthUserDto;
import com.gg.mafia.domain.member.exception.SignupFailedException;
import com.gg.mafia.global.config.security.jwt.TokenProvider;
import com.gg.mafia.global.config.security.oauth.GoogleStrategy;
import com.gg.mafia.global.config.security.oauth.KakaoStrategy;
import com.gg.mafia.global.config.security.oauth.NaverStrategy;
import com.gg.mafia.global.config.security.oauth.OAuthStrategy;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OAuthService {
    private final GoogleStrategy googleStrategy;
    private final KakaoStrategy kakaoStrategy;
    private final NaverStrategy naverStrategy;
    private final UserDao userDao;
    private final TokenProvider tokenProvider;

    public String signupOrLoginByCode(String code, String oAuthType) {
        OAuthStrategy strategy = getStrategy(oAuthType);
        String oAuthAccessToken = strategy.getAccessToken(code);
        OAuthUserDto oAuthUserDto = strategy.getOAuthUser(oAuthAccessToken);
        return signupOrLogin(oAuthUserDto);
    }

    private String signupOrLogin(OAuthUserDto oAuthUserDto) {
        User user = getOrCreateUser(oAuthUserDto);
        return tokenProvider.createToken(user.getEmail());
    }

    private OAuthStrategy getStrategy(String oAuthType) {
        OAuthStrategy strategy;
        if (oAuthType.equals(OAuthStrategy.GOOGLE)) {
            strategy = googleStrategy;
        } else if(oAuthType.equals(OAuthStrategy.KAKAO)){
            strategy = kakaoStrategy;
        } else if(oAuthType.equals(OAuthStrategy.NAVER)){
            strategy = naverStrategy;
        } else {
            throw new IllegalArgumentException("OAuth 타입 불명");
        }
        return strategy;
    }

    private User getOrCreateUser(OAuthUserDto oAuthUserDto) {
        Optional<User> user = userDao.findByEmail(oAuthUserDto.getEmail());
        if (user.isEmpty()) {
            return createUser(oAuthUserDto);
        }
        validateOAuthUserType(user.get(), oAuthUserDto);
        return user.get();
    }

    private User createUser(OAuthUserDto oAuthUserDto) {
        User user = User.builder()
                .email(oAuthUserDto.getEmail())
                .password(oAuthUserDto.getStrategyCode())
                .build();
        userDao.save(user);
        return user;
    }

    private void validateOAuthUserType(User user, OAuthUserDto oAuthUserDto) {
        String strategyCode = user.getPassword();
        if (!strategyCode.equals(oAuthUserDto.getStrategyCode())) {
            throw new SignupFailedException();
        }
    }
}
