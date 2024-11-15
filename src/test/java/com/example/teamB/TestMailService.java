package com.example.teamB;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@SpringBootTest
public class TestMailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendTestEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your-email@gmail.com");
        message.setTo("recipient-email@gmail.com");
        message.setSubject("Test Email");
        message.setText("This is a test email from Spring Boot.");

        mailSender.send(message);
    }
}