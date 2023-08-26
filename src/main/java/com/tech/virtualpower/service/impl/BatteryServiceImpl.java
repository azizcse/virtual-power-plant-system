package com.tech.virtualpower.service.impl;


import com.tech.virtualpower.exception.ResourceNotFoundException;
import com.tech.virtualpower.model.Battery;
import com.tech.virtualpower.payload.BatteryDto;
import com.tech.virtualpower.payload.PagedResponse;
import com.tech.virtualpower.repository.BatteryRepository;
import com.tech.virtualpower.service.BatteryService;
import com.tech.virtualpower.util.AppUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BatteryServiceImpl implements BatteryService {
    Logger LOGGER = LoggerFactory.getLogger(BatteryServiceImpl.class);
    @Autowired
    private BatteryRepository repository;

    @Override
    public void register(Battery battery) {
        LOGGER.info("** Battery name : " + battery.getName());
        Battery b = repository.save(battery);
        System.out.println("Battery id : " + b.toString());
    }

    @Override
    public List<BatteryDto> getAllBatteries() {
        List<Battery> batteries = repository.findAll();
        List<BatteryDto> dtoList = new ArrayList<>();
        batteries.forEach(item -> {
            BatteryDto b = new BatteryDto(item.getName(), item.getPostcode(), item.getCapacity());
            dtoList.add(b);
        });
        return dtoList;
    }

    @Override
    public PagedResponse<Battery> getAll(int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Battery> batteries = repository.findAll(pageable);
        List<Battery> content = batteries.getNumberOfElements() == 0 ? Collections.emptyList() : batteries.getContent();
        return new PagedResponse<>(content, batteries.getNumber(), batteries.getSize(), batteries.getTotalElements(), batteries.getTotalPages(),batteries.isLast());
    }

    @Override
    public Map<String, Object> getBatteriesInPostcodeRange(String startPostcode, String endPostcode, Integer minCapacity, Integer maxCapacity) {
        List<Battery> batteries =  repository.findByPostcodeBetweenOrderByNameAsc(startPostcode, endPostcode);
        // Filter batteries based on minCapacity and maxCapacity
        if (minCapacity != null) {
            batteries = batteries.stream()
                    .filter(battery -> battery.getCapacity() >= minCapacity)
                    .collect(Collectors.toList());
        }
        if (maxCapacity != null) {
            batteries = batteries.stream()
                    .filter(battery -> battery.getCapacity() <= maxCapacity)
                    .collect(Collectors.toList());
        }

        double totalCapacity = calculateTotalCapacity(batteries);
        double averageCapacity = calculateAverageCapacity(batteries);

        Map<String, Object> response = new HashMap<>();
        response.put("batteries", batteries);
        response.put("totalCapacity", totalCapacity);
        response.put("averageCapacity", averageCapacity);
        return response;
    }

    private double calculateTotalCapacity(List<Battery> batteries) {
        return batteries.stream().mapToDouble(Battery::getCapacity).sum();
    }

    private double calculateAverageCapacity(List<Battery> batteries) {
        return batteries.stream().mapToDouble(Battery::getCapacity).average().orElse(0.0);
    }

    @Override
    public PagedResponse<Battery> findByCapacityLessThan(int minCapacity,int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Battery> batteries = repository.findByCapacityLessThan(minCapacity,pageable);
        List<Battery> content = batteries.getNumberOfElements() == 0 ? Collections.emptyList() : batteries.getContent();
        return new PagedResponse<>(content, batteries.getNumber(), batteries.getSize(), batteries.getTotalElements(), batteries.getTotalPages(),batteries.isLast());
    }

    @Override
    public Battery updateBattery(Long id, Battery battery) {
        Battery item = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("POST", "ID", id));
        item.setName(battery.getName());
        item.setPostcode(battery.getPostcode());
        item.setCapacity(battery.getCapacity());
        return repository.save(item);
    }


}
