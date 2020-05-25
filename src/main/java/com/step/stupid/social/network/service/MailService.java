package com.step.stupid.social.network.service;

public interface MailService {

    void send(String toEmail, String subject, String text);

    void sendGroupMessage(String[] emails, String subject, String text);

    void sendMessageWithAttachment(String emailTo, String subject, String text, String pathToAttachment);
}
