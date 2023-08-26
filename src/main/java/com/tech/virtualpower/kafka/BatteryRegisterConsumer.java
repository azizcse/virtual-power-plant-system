package com.tech.virtualpower.kafka;

import com.tech.virtualpower.model.Battery;
import com.tech.virtualpower.payload.BatteryDto;
import com.tech.virtualpower.service.BatteryService;
import com.tech.virtualpower.util.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatteryRegisterConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(BatteryRegisterConsumer.class);
    @Autowired
    private BatteryService batteryService;
    @KafkaListener(topics = "battery_register", groupId = "battery")
    public void registerConsume(String batteryRequests){
        List<BatteryDto> list = GsonUtil.on().toModelFromJson(batteryRequests);
        list.forEach(item->batteryService.register(new Battery(item.getName(),item.getPostcode(),item.getCapacity())));
        LOGGER.info(String.format("Kafka registered battery count -> "+list.size()));
    }
}
