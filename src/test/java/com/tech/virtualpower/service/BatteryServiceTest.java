package com.tech.virtualpower.service;

import com.tech.virtualpower.controller.BatteryController;
import com.tech.virtualpower.model.Battery;

import com.tech.virtualpower.payload.BatteryDto;
import com.tech.virtualpower.payload.BatteryRegisterResponse;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RunWith(MockitoJUnitRunner.class)
public class BatteryServiceTest {

    @Mock
    private BatteryService batteryService;
    @InjectMocks
    private BatteryController controller;
    @BeforeEach
    public void setUp(){

    }

    @Test
    public void testGetBatteriesInPostcodeRangeSuccess(){
        List<BatteryDto> mockBatteryList = new ArrayList<>();
        mockBatteryList.add(new BatteryDto("AAAAAA","6000",23500));
        mockBatteryList.add(new BatteryDto("BBBBB","7000",3500));
        Map<String, Object> mockResult = new HashMap<>();
        mockResult.put("batteries", mockBatteryList);
        mockResult.put("totalCapacity", 50000);
        mockResult.put("averageCapacity", 40050);
        ResponseEntity<Map<String, Object>> mockResponse = new ResponseEntity<>(mockResult,HttpStatus.OK);

        when(batteryService.getBatteriesInPostcodeRange("150","250", 20,30)).thenReturn(mockResult);

        ResponseEntity<Map<String, Object>> mockResponseEntity = controller.getBatteriesInPostcodeRange("150","250", 20,30);
        assertEquals(200, mockResponseEntity.getStatusCodeValue());
        assertEquals(mockResponse, mockResponseEntity.getBody());

    }



    @Test
    public void testGetBatteriesInPostcodeRangeFail(){

        Map<String, Object> mockResult = new HashMap<>();

        ResponseEntity<Map<String, Object>> mockResponse = new ResponseEntity<>(mockResult,HttpStatus.BAD_REQUEST);

        when(batteryService.getBatteriesInPostcodeRange(null,"250", 20,30)).thenReturn(mockResult);

        ResponseEntity<Map<String, Object>> mockResponseEntity = controller.getBatteriesInPostcodeRange(null,"250", 20,30);
        assertEquals(400, mockResponseEntity.getStatusCodeValue());
        assertEquals(mockResponse, mockResponseEntity.getBody());

    }
}
