package com.scm.scm20.services.impl;

import com.scm.scm20.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl  implements EmailService
{

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.properties.domain_name}")
    private String domain_name;

    @Override
    public void sendEmail(String to, String subject, String body) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);

        // Use a sender from your Mailtrap domain
        simpleMailMessage.setFrom(domain_name);

        javaMailSender.send(simpleMailMessage);
        System.out.println("Email sent to: " + to);
    }

//    @Override
//    public void sendEmailWithHtml() {
//
//    }
//
//    @Override
//    public void sendEmailWithAttachment() {
//
//    }
}
