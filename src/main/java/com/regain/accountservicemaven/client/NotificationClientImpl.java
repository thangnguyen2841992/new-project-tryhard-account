package com.regain.accountservicemaven.client;

import com.regain.accountservicemaven.model.dto.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificationClientImpl implements INotificationClient {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void sendNotificationEmail(MessageDTO messageDTO) {
        logger.error("Notification service is slow");
    }
}
