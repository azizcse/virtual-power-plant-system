package com.tech.virtualpower.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
public class BatteryRegisterProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(BatteryRegisterProducer.class);
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public BatteryRegisterProducer(KafkaTemplate<String, String> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }
    public void registerBattery(String  messages){
        LOGGER.info("Kafka event trigger");
        ListenableFuture<SendResult<String,String>> value= kafkaTemplate.send("battery_register",messages);

    }
}
