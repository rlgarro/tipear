package com.roman.tipear.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String receiver, String body, String subject) throws MailException {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("garrodev18@gmail.com");
        message.setTo(receiver);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);
    }
}
