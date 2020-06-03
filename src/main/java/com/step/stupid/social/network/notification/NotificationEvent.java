package com.step.stupid.social.network.notification;

import lombok.Data;

@Data
public class NotificationEvent {

    private String subject;
    private String text;
    private String notificationType;
    private String id;
}
