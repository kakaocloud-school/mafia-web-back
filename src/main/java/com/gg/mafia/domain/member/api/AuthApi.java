package com.gg.mafia.domain.member.api;

import com.gg.mafia.domain.member.application.AuthService;
import com.gg.mafia.domain.member.application.MailService;
import com.gg.mafia.domain.member.dto.ConfirmMailRequest;
import com.gg.mafia.domain.member.dto.LoginRequest;
import com.gg.mafia.domain.member.dto.SendMailRequest;
import com.gg.mafia.domain.member.dto.SignupRequest;
import com.gg.mafia.global.common.response.ApiResponse;
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
        authService.signup(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse<String>> authenticate(@RequestBody @Validated LoginRequest request) {
        String result = authService.authenticate(request);
        return ResponseEntity.ok().body(ApiResponse.success(result));
    }

    @PostMapping("/sendMail")
    public ResponseEntity<ApiResponse<String>> sendMail(@RequestBody @Validated SendMailRequest request){
        String authCode = mailService.sendEmail(request);
        return ResponseEntity.ok().body(ApiResponse.success(authCode));
    }

    @PostMapping("/confirmMail")
    public ResponseEntity<ApiResponse<Boolean>> confirmMail(@RequestBody @Validated ConfirmMailRequest request){
        Boolean isCodeMatching = mailService.confirmMail(request);
        return ResponseEntity.ok().body(ApiResponse.success(isCodeMatching));
    }
}
