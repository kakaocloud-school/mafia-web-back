package com.gg.mafia.global.config.security;

import com.gg.mafia.domain.member.dao.UserDao;
import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.member.dto.UserDetailsImpl;
import com.gg.mafia.domain.member.dto.UserMapper;
import com.gg.mafia.domain.member.exception.LoginRequiredException;
import com.gg.mafia.global.util.Util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class TokenProvider {
    private final Key key;
    private final Long tokenValidityInSeconds;
    private final UserDao userDao;
    private final UserMapper userMapper;

    public TokenProvider(Environment env, UserDao userDao, UserMapper userMapper) {
        String secret = env.getProperty("jwt.secret");
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.tokenValidityInSeconds = Util.getLongProperty(env, "jwt.token-validity-in-seconds");
        this.userDao = userDao;
        this.userMapper = userMapper;
    }

    public String createToken(Authentication authentication) {
        return Jwts.builder()
                .setSubject(authentication.getName())
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(getExpiration())
                .setIssuedAt(new Date())
                .compact();
    }

    @Transactional
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        String email = claims.getSubject();
        User user = findUserByEmail(email);
        UserDetailsImpl principle = userMapper.toPrinciple(user);
        return new UsernamePasswordAuthenticationToken(principle, token, principle.getAuthorities());
    }

    private Date getExpiration() {
        long nowMilliSeconds = new Date().getTime();
        long tokenValidityInMilliSeconds = tokenValidityInSeconds * 1000;
        long expirationMilliSeconds = nowMilliSeconds + tokenValidityInMilliSeconds;
        return new Date(expirationMilliSeconds);
    }

    private Claims getClaims(String token) {
        Claims claims;
        try {
            JwtParser jwtParser = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build();
            claims = jwtParser
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException | MalformedJwtException e) {
            throw new MalformedJwtException("잘못된 JWT 서명입니다");
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "만료된 JWT 토큰입니다");
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException("지원되지 않는 JWT 토큰입니다");
        } catch (IllegalArgumentException e) {
            throw new MalformedJwtException("JWT 토큰이 잘못되었습니다");
        }
        return claims;
    }

    private User findUserByEmail(String email) {
        Optional<User> user = userDao.findByEmail(email);
        if (user.isEmpty()) {
            throw new LoginRequiredException();
        }
        return user.get();
    }
}
