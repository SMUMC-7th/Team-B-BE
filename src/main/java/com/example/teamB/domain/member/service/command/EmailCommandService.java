package com.example.teamB.domain.member.service.command;

import jakarta.mail.MessagingException;

public interface EmailCommandService {
    String sendVerificationEmail(String toEmail) throws MessagingException;
    String generateVerificationCode();
}
