package com.gg.mafia.domain.member.api;

import com.gg.mafia.domain.member.application.AuthService;
import com.gg.mafia.domain.member.application.MailService;
import com.gg.mafia.domain.member.application.OAuthService;
import com.gg.mafia.domain.member.dto.ConfirmMailRequest;
import com.gg.mafia.domain.member.dto.LoginRequest;
import com.gg.mafia.domain.member.dto.SendMailRequest;
import com.gg.mafia.domain.member.dto.SignupRequest;
import com.gg.mafia.domain.member.exception.UserNotAllowedException;
import com.gg.mafia.global.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    private final MailService mailService;


    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<Void>> signup(@RequestBody @Validated SignupRequest request) {
        if(!mailService.confirmMail(request.getEmail(),request.getEmailCode())){
            throw new UserNotAllowedException("인증되지 않은 사용자입니다.");
        }
        authService.signup(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse<String>> authenticate(@RequestBody @Validated LoginRequest request) {
        String result = authService.authenticate(request);
        return ResponseEntity.ok().body(ApiResponse.success(result));
    }

    @PostMapping("/send-mail")
    public ResponseEntity<ApiResponse<String>> sendMail(@RequestBody @Validated SendMailRequest request, HttpServletRequest servletRequest){
        String clientIP = servletRequest.getHeader("X-Forwarded-For");

        // X-Forwarded-For 헤더가 없거나 비어 있는 경우, 클라이언트의 IP 주소는 remoteAddr로부터 가져옴
        if (clientIP == null || clientIP.isEmpty()) {
            clientIP = servletRequest.getRemoteAddr();
        }

        mailService.sendEmail(request, clientIP);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm-mail")
    public ResponseEntity<ApiResponse<Boolean>> confirmMail(@RequestBody @Validated ConfirmMailRequest request){
        Boolean isCodeMatching = mailService.confirmMail(request.getEmail(),request.getEmailCode());
        return ResponseEntity.ok().body(ApiResponse.success(isCodeMatching));
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
            String encodedErrorMessage = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            response.sendRedirect("%s?error=%s".formatted(redirectUrl, encodedErrorMessage));
        }
    }
}
