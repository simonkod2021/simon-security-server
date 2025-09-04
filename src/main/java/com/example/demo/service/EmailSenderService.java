package com.example.demo.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailSenderService {
    private final JavaMailSender mailSender;

    public EmailSenderService(JavaMailSender mailSender) throws IOException {
        this.mailSender = mailSender;
    }
    public void sendEmail(String email, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("simon98larsson@gmail.com");
        message.setTo(email);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);

        System.out.println("Mail sent successfully...");
    }
}
