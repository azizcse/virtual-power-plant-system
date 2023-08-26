package com.tech.virtualpower.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.stereotype.Service;


@Service
public class BatteryCapacityUpdateProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(BatteryCapacityUpdateProducer.class);
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public BatteryCapacityUpdateProducer(KafkaTemplate<String, String> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendBatteryCapacityUpdate(String  messages){
        LOGGER.info("Kafka capacity update for"+messages);
        kafkaTemplate.send("battery_update",messages);

    }
}
