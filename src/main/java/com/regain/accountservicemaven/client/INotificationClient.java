package com.regain.accountservicemaven.client;

import com.regain.accountservicemaven.model.dto.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", url = "http://localhost:8081", fallback = NotificationClientImpl.class)
public interface INotificationClient {
        @PostMapping("/sendNotificationEmail.do")
        void sendNotificationEmail(@RequestBody MessageDTO messageDTO);
}
