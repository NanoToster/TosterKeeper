package ru.vsu.services.mail;

import org.springframework.mail.MailException;

/**
 * @author Ivan Rovenskiy
 * 01 March 2020
 */
public interface EmailService {
    void sendMessageWithAuthorizationCode(String to, String code) throws MailException;
}