package com.tech.virtualpower.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic batteryRegisterTopic(){
        return TopicBuilder.name("battery_register")
                .build();
    }

    @Bean
    public NewTopic batteryCapacityUpdateTopic(){
        return TopicBuilder.name("battery_update")
                .build();
    }
}
