package com.tech.virtualpower.controller;

import com.tech.virtualpower.kafka.BatteryCapacityUpdateProducer;
import com.tech.virtualpower.kafka.BatteryRegisterProducer;

import com.tech.virtualpower.model.Battery;
import com.tech.virtualpower.payload.BatteryDto;
import com.tech.virtualpower.payload.BatteryRegisterResponse;
import com.tech.virtualpower.service.BatteryService;
import com.tech.virtualpower.util.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v2/battery")
public class KafkaBatteryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaBatteryController.class);
    @Autowired
    private BatteryRegisterProducer kafkaRegisterProducer;
    @Autowired
    private BatteryCapacityUpdateProducer batteryCapacityUpdateProducer;
    @Autowired
    private BatteryService batteryService;

    @Autowired
    public KafkaBatteryController(BatteryRegisterProducer producer) {
        this.kafkaRegisterProducer = producer;
    }

    @PostMapping("/register")
    public ResponseEntity<BatteryRegisterResponse> register(@RequestBody List<BatteryDto> batteryRequests) {
        String jsonStr = GsonUtil.on().toStringFromModel(batteryRequests);
        kafkaRegisterProducer.registerBattery(jsonStr);
        return new ResponseEntity<>(new BatteryRegisterResponse(batteryRequests.size(), "Kafka message has sent to register"), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Battery> updateBattery(@PathVariable(name = "id") Long id, @Valid @RequestBody BatteryDto batteryDto){
        Battery battery = batteryService.updateBattery(id, new Battery(batteryDto.getName(),batteryDto.getPostcode(),batteryDto.getCapacity()));
        String jsonStr = GsonUtil.on().toStringFromBattery(batteryDto);
        //Kafka battery capacity update producer
        batteryCapacityUpdateProducer.sendBatteryCapacityUpdate(jsonStr);
        return new ResponseEntity<>(battery, HttpStatus.OK);
    }
}
