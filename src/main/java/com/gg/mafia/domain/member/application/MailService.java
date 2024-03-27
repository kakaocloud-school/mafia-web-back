package com.gg.mafia.domain.member.application;

import com.gg.mafia.domain.member.dao.UserDao;
import com.gg.mafia.domain.member.dto.SendMailRequest;
import com.gg.mafia.domain.member.exception.UserAlreadyExistsException;
import com.gg.mafia.infra.smtp.SmtpMailSender;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    @Autowired
    private SmtpMailSender smtpMailSender;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    private String authCode;
    private final UserDao userDao;

    public void makeEmailAuthCode() {
        Random rnd = new Random();
        String randomNumber = "";
        for(int i = 0; i < 6; i++) {
            randomNumber += Integer.toString(rnd.nextInt(10));
        }

        authCode = randomNumber;
    }
    public void sendEmail(SendMailRequest request,String clientIP) {
        if(userDao.findByEmail(request.getEmail()).isPresent()){
            throw new UserAlreadyExistsException();
        }
        makeEmailAuthCode();

        String title = "회원 가입 인증 이메일 입니다.";
        String content =
                "<h1>MAFIA.GG에 오신 것을 환영합니다.</h1>" +
                        "<br><br>" +
                        "인증 번호는 " + authCode + "입니다." +
                        "<br>" +
                        "인증번호를 사이트에 입력해주세요.";

        smtpMailSender.sendEmail(request.getEmail(),clientIP,title,content);

        redisTemplate.opsForValue().set(request.getEmail(), authCode,5, TimeUnit.MINUTES);

    }

    public Boolean confirmMail(String email,String emailCode) {
        String authCode = (String)redisTemplate.opsForValue().get(email);

        return emailCode.equals(authCode);
    }

}
