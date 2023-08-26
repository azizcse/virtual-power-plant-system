package com.tech.virtualpower.kafka;

import com.tech.virtualpower.payload.BatteryDto;
import com.tech.virtualpower.util.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BatteryCapacityUpdateConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(BatteryCapacityUpdateConsumer.class);
    @KafkaListener(topics = "battery_update", groupId = "update")
    public void capacityUpdateConsume(String battery){
        BatteryDto item = GsonUtil.on().toBatteryFromJson(battery);
        LOGGER.info(String.format("Battery capacity updated for -> %s", item.toString()));
    }
}
