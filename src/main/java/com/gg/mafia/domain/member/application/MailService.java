package com.gg.mafia.domain.member.application;

import com.gg.mafia.domain.member.dao.UserDao;
import com.gg.mafia.domain.member.dto.SendMailRequest;
import com.gg.mafia.domain.member.exception.MailServerException;
import com.gg.mafia.domain.member.exception.RequestThrottlingException;
import com.gg.mafia.domain.member.exception.UserAlreadyExistsException;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ThrottlingService throttlingService;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    private String authCode;
    private final UserDao userDao;
    private final Environment env;

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
        String setFromName = "MAFIA.GG";
        String setFrom = env.getProperty("mail.username");
        String toMail = request.getEmail();
        String title = "회원 가입 인증 이메일 입니다.";
        String content =
                "<h1>MAFIA.GG에 오신 것을 환영합니다.</h1>" +
                        "<br><br>" +
                        "인증 번호는 " + authCode + "입니다." +
                        "<br>" +
                        "인증번호를 사이트에 입력해주세요.";
        mailSend(setFrom,setFromName, toMail, title, content, clientIP);

        redisTemplate.opsForValue().set(toMail, authCode,5, TimeUnit.MINUTES);

    }

    public void mailSend(String setFrom, String setFromName, String toMail, String title, String content,String clientIP) {
        MimeMessage message = mailSender.createMimeMessage();//JavaMailSender 객체를 사용하여 MimeMessage 객체를 생성
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");//이메일 메시지와 관련된 설정을 수행합니다.
            // true를 전달하여 multipart 형식의 메시지를 지원하고, "utf-8"을 전달하여 문자 인코딩을 설정
            helper.setFrom(setFrom,setFromName);//이메일의 발신자 주소 설정
            helper.setTo(toMail);//이메일의 수신자 주소 설정
            helper.setSubject(title);//이메일의 제목을 설정
            helper.setText(content,true);//이메일의 내용 설정 두 번째 매개 변수에 true를 설정하여 html 설정으로한다.
            if(throttlingService.allowRequest(clientIP)){
                mailSender.send(message);
            }else{
                throw new RequestThrottlingException("ip - "+clientIP+"= too many request");
            }

        } catch (MessagingException e) {
            throw new MailServerException();
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("인코딩 오류 발생");
        }
    }

    public Boolean confirmMail(String email,String emailCode) {
        String authCode = (String)redisTemplate.opsForValue().get(email);

        return emailCode.equals(authCode);
    }

}
