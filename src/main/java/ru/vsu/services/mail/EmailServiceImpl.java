package ru.vsu.services.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author Ivan Rovenskiy
 * 01 March 2020
 */
@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final String fromEmail;


    public EmailServiceImpl(JavaMailSender mailSender, @Value("${mail.address.from}") String fromEmail) {
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
    }

    @Override
    public void sendMessageWithAuthorizationCode(String to, String code) throws MailException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(fromEmail);
        message.setSubject("Auth code");
        message.setText("Your authorization code is: " + code);
        mailSender.send(message);
    }
}