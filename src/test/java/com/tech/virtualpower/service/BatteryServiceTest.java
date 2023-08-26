package com.tech.virtualpower.service;

import com.tech.virtualpower.repository.BatteryRepository;
import com.tech.virtualpower.service.BatteryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BatteryServiceTest {
    @Mock
    private BatteryRepository repository;

    @InjectMocks
    private BatteryService batteryService;
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }
}
