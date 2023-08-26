package com.tech.virtualpower.controller;

import com.tech.virtualpower.service.BatteryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestKafkaBatteryController {
    @Mock
    private BatteryService batteryService;


    @InjectMocks
    private KafkaBatteryController kafkaBatteryController;


    @BeforeEach
    public void setUp(){

    }
}
