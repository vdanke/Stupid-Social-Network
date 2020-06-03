package com.step.stupid.social.network.service.impl;

import com.step.stupid.social.network.mapper.jsonb.JsonMapper;
import com.step.stupid.social.network.notification.NotificationEvent;
import com.step.stupid.social.network.repository.UserRepository;
import com.step.stupid.social.network.service.ReceiveMessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReceiverMessageHandlerImpl implements ReceiveMessageHandler {

    private final JsonMapper jsonMapper;
    private final UserRepository userRepository;

    @Override
    public void handleMessage(String topic) {
        NotificationEvent notificationEvent = jsonMapper.deserializeToNotificationEvent(topic);

        log.info(notificationEvent.toString());
    }
}
