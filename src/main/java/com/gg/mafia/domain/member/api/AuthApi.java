package com.gg.mafia.domain.member.api;

import com.gg.mafia.domain.member.application.AuthService;
import com.gg.mafia.domain.member.application.MailService;
import com.gg.mafia.domain.member.dto.ConfirmMailRequest;
import com.gg.mafia.domain.member.dto.LoginRequest;
import com.gg.mafia.domain.member.dto.SendMailRequest;
import com.gg.mafia.domain.member.dto.SignupRequest;
import com.gg.mafia.domain.member.exception.UserNotAllowedException;
import com.gg.mafia.global.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class AuthApi {
    private final AuthService authService;

    private final MailService mailService;


    @PostMapping("/signup")
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

    @PostMapping("/sendMail")
    public ResponseEntity<ApiResponse<String>> sendMail(@RequestBody @Validated SendMailRequest request, HttpServletRequest servletRequest){
        String clientIP = servletRequest.getHeader("X-Forwarded-For");

        // X-Forwarded-For 헤더가 없거나 비어 있는 경우, 클라이언트의 IP 주소는 remoteAddr로부터 가져옴
        if (clientIP == null || clientIP.isEmpty()) {
            clientIP = servletRequest.getRemoteAddr();
        }

        mailService.sendEmail(request, clientIP);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirmMail")
    public ResponseEntity<ApiResponse<Boolean>> confirmMail(@RequestBody @Validated ConfirmMailRequest request){
        Boolean isCodeMatching = mailService.confirmMail(request.getEmail(),request.getEmailCode());
        return ResponseEntity.ok().body(ApiResponse.success(isCodeMatching));
    }
}
