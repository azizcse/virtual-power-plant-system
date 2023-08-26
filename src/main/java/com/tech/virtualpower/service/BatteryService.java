package com.tech.virtualpower.service;

import com.tech.virtualpower.model.Battery;
import com.tech.virtualpower.payload.BatteryDto;
import com.tech.virtualpower.payload.PagedResponse;

import java.util.List;
import java.util.Map;

public interface BatteryService {
    Battery register(Battery battery);
    List<BatteryDto> getAllBatteries();
    PagedResponse<Battery> getAll(int page, int size);
    Map<String, Object> getBatteriesInPostcodeRange(String startPostcode, String endPostcode, Integer min, Integer max);
    PagedResponse<Battery> findByCapacityLessThan(int minCapacity,int page, int size);

    Battery updateBattery(Long id, Battery battery);
}
