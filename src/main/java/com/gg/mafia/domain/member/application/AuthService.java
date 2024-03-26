package com.gg.mafia.domain.member.application;

import com.gg.mafia.domain.member.dao.UserDao;
import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.member.dto.LoginRequest;
import com.gg.mafia.domain.member.dto.SignupRequest;
import com.gg.mafia.domain.member.dto.UserMapper;
import com.gg.mafia.domain.member.exception.LoginFailedException;
import com.gg.mafia.domain.profile.domain.Profile;
import com.gg.mafia.global.config.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;

    public void signup(SignupRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodedPassword);

        User user = userMapper.toEntity(request);

        Profile profile = Profile.builder()
                .user(user)
                .userName(request.getNickname())
                .build();

        user.setProfile(profile);

        userDao.save(user);

    }

    public String authenticate(LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        Authentication authentication;
        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new LoginFailedException("비밀번호가 일치하지 않습니다");
        } catch (InternalAuthenticationServiceException e) {
            throw new LoginFailedException("존재하지 않는 유저입니다");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.createToken(authentication);
    }
}
