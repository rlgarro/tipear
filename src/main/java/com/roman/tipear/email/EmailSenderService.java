package com.roman.tipear.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(Boolean isForActivation, String username, String receiver, String url, String subject) throws MailException {

        MimeMessage message = mailSender.createMimeMessage();
        try {

            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

            messageHelper.setFrom("garrodev18@gmail.com");
            messageHelper.setTo(receiver);
            messageHelper.setSubject(subject);

            // Messages I use for emails in a string, formatting a html file
            String activationMessageContent = "<div style='width: 100%; height: 600px; background-color: #282828; color: #ccc;'><h1 style='padding:50px;width: 100%; text-align:center;font-size:35px;color: #ccc;'>Hey " + username + ", welcome to Tipear!</h1><hr><br>" +
                    "<h2 style='text-align: center;color:#ccc; font-size: 20px;'>Activate your account by clicking the button or the link.</h2><br>"
                    + "<div style='width: 100%; height: 150px; display: flex;justify-content: center'><a style='width: 130px;height: 50px;margin-left: 250px;font-weight: bold;border-color: #282828;border-radius: 5px;font-size: 32px;background-color: #555; color: #ccc;text-decoration:none;padding: 5px; margin-right: 50px; display: flex; justify-content: center; align-items: center; color: #b8bb26' target='_blank' href='"+url+"'>Activate</a><br><br>" +
                    "<a href='"+url+"'style='font-size:22px;text-align: center'>" + url + "</h2></div></div>";

            String passwordMessageContent = "<div style='width: 100%; height: 600px; background-color: #282828; color: #ccc;'><h1 style='padding:50px;width: 100%; color:#ccc;text-align:center;font-size:35px'>Password recovery<h1><hr><br>" +
                    "<h2 style='text-align: center; font-size: 20px;color:#ccc'>Reset your password by clicking the button or the link.</h2><br>"
                    + "<div style='width: 100%; height: 150px; display: flex;justify-content: center'><a style='width: 90px;height: 50px;margin-left: 250px;font-weight: bold;border-color: #282828;border-radius: 5px;font-size: 32px;background-color: #555; color: #ccc;text-decoration:none;padding: 5px; margin-right: 50px; display: flex; justify-content: center; align-items: center; color: #b8bb26' target='_blank' href='"+url+"'>Reset</a><br><br>" +
                    "<a href='"+url+"' style='font-size:22px;text-align: center'>" + url + "</a></div></div>";


            if (isForActivation) {
                message.setContent(activationMessageContent, "text/html");
            } else {
                message.setContent(passwordMessageContent, "text/html");
            }
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
