package com.regain.accountservicemaven.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfiguration {
    @Bean
    NewTopic checkEmailTopic() {
        return new NewTopic("send-email-active",2,(short) 1);
    }
}
