package com.example.demo.controllers;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {
    private final JavaMailSender sender;

    public EmailController(JavaMailSender sender) {
        this.sender = sender;
    }

    @RequestMapping("/send-email")
    public String sendEmail(){
        try{
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("simon98larsson@gmail.com");
        message.setTo("simon98larsson@gmail.com");
        message.setSubject("Hello World");
        message.setText("Hello World");
        sender.send(message);
        return "Email sent";
    }catch (Exception e){
        return "Something went wrong";}
    }
}
