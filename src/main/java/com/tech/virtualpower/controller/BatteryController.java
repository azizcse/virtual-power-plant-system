package com.tech.virtualpower.controller;

import com.tech.virtualpower.kafka.BatteryCapacityUpdateProducer;
import com.tech.virtualpower.manager.BackgroundTaskManager;

import com.tech.virtualpower.model.Battery;
import com.tech.virtualpower.payload.BatteryDto;
import com.tech.virtualpower.payload.BatteryRegisterResponse;
import com.tech.virtualpower.payload.PagedResponse;
import com.tech.virtualpower.service.BatteryService;
import com.tech.virtualpower.util.AppConstants;

import com.tech.virtualpower.util.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;


@RestController
@RequestMapping("/api/v1/battery")
public class BatteryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BatteryController.class);
    private BackgroundTaskManager backgroundTaskService;
    private BatteryService batteryService;

    @Autowired
    public BatteryController(BatteryService batteryService) {
        this.batteryService = batteryService;
        configureBackgroundTaskExecutor();
    }

    @PostMapping("/register")
    public ResponseEntity<BatteryRegisterResponse> registerBattery(@Valid @RequestBody List<BatteryDto> batteryRequests) {
        startBackgroundRegistrationProcess(batteryRequests);
        return new ResponseEntity<>(new BatteryRegisterResponse(batteryRequests.size(), "Battery register progressing"), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<PagedResponse<Battery>> getAll(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
        PagedResponse<Battery> response = batteryService.getAll(page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search")
    @Cacheable("searchBatteryRangeCache")
    public ResponseEntity<Map<String, Object>> getBatteriesInPostcodeRange(@RequestParam String startPostcode,
                                                                           @RequestParam String endPostcode,
                                                                           @RequestParam(required = false) Integer minCapacity,
                                                                           @RequestParam(required = false) Integer maxCapacity) {

        Map<String, Object> response = batteryService.getBatteriesInPostcodeRange(startPostcode, endPostcode, minCapacity, maxCapacity);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/below-capacity/{threshold}")
    @Cacheable("belowCapacityCache")
    public ResponseEntity<PagedResponse<Battery>> getBatteriesBelowCapacity(
            @PathVariable int threshold,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
        PagedResponse<Battery> response = batteryService.findByCapacityLessThan(threshold, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    private void configureBackgroundTaskExecutor() {
        ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("Battery " + this.hashCode() + " Output");
                thread.setDaemon(true);
                return thread;
            }
        });

        backgroundTaskService = new BackgroundTaskManager(pool);
    }


    private void startBackgroundRegistrationProcess(List<BatteryDto> batteryDtos) {
        new Thread(() -> {
            batteryDtos.forEach(item -> {
                backgroundTaskService.addAppTaskInQueue(new RegistrationTask(item));
            });
        }).start();
    }

    class RegistrationTask implements Runnable {
        private final BatteryDto batteryRequest;

        public RegistrationTask(BatteryDto batteryRequest) {
            this.batteryRequest = batteryRequest;
        }

        @Override
        public void run() {
            Battery battery = new Battery(batteryRequest.getName(), batteryRequest.getPostcode(), batteryRequest.getCapacity());
            batteryService.register(battery);
            LOGGER.info("Battery registered :" + batteryRequest.toString());
        }
    }

}
