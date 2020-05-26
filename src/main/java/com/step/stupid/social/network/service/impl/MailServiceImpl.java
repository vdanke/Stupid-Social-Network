package com.step.stupid.social.network.service.impl;

import com.step.stupid.social.network.exception.MessageSendException;
import com.step.stupid.social.network.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Override
    public void send(String toEmail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(emailFrom);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(text);

        javaMailSender.send(message);
    }

    @Override
    public void sendGroupMessage(String[] emails, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

//        message.setFrom(emailFrom);
        message.setTo(emails);
        message.setSubject(subject);
        message.setText(text);

        javaMailSender.send(message);
    }

    @Override
    public void sendMessageWithAttachment(String emailTo, String subject, String text, String pathToAttachment) {
        final String errorMessage = "Sorry, something is wrong with our server, please wait till we fix it. Call our " +
                "agent 777777777";

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

//            helper.setFrom(emailFrom);
            helper.setTo(emailTo);
            helper.setSubject(subject);
            helper.setText(text);

            FileSystemResource file = new FileSystemResource(new File(pathToAttachment));

            helper.addAttachment("Contract", file);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            this.send(emailTo, subject, errorMessage);
            throw new MessageSendException(errorMessage);
        }
    }
}
