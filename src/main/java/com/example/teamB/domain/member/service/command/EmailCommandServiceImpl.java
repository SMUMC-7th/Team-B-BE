package com.example.teamB.domain.member.service.command;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailCommandServiceImpl implements EmailCommandService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public String sendVerificationEmail(String toEmail) throws MessagingException {
        String verificationCode = generateVerificationCode();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject("[클로디] 인증 코드 ");
        helper.setText("인증코드 : " + verificationCode);

        mailSender.send(message);

        return verificationCode;
    }

    public String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999999)); // 6자리 인증 코드 생성
    }
}