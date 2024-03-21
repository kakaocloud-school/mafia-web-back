package com.gg.mafia.domain.member.api;

import com.gg.mafia.domain.member.application.AuthService;
import com.gg.mafia.domain.member.application.OAuthService;
import com.gg.mafia.domain.member.dto.LoginRequest;
import com.gg.mafia.domain.member.dto.SignupRequest;
import com.gg.mafia.global.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class AuthApi {
    private final AuthService authService;
    private final OAuthService oAuthService;
    private final Environment env;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@RequestBody @Validated SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse<String>> authenticate(@RequestBody @Validated LoginRequest request) {
        String result = authService.authenticate(request);
        return ResponseEntity.ok().body(ApiResponse.success(result));
    }

    @GetMapping("/oauth-types/{oAuthType}/validate-oauth2-code")
    public void validateOAuth2Code(
            @RequestParam String code,
            @PathVariable String oAuthType,
            HttpServletResponse response
    ) throws IOException {
        String redirectUrl = env.getProperty("front-uri");
        try {
            String token = oAuthService.signupOrLoginByCode(code, oAuthType);
            response.sendRedirect("%s?token=%s".formatted(redirectUrl, token));
        } catch (Exception e) {
            response.sendRedirect("%s?error=%s".formatted(redirectUrl, e.getMessage()));
        }
    }
}
