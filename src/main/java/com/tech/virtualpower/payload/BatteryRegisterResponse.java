package com.tech.virtualpower.payload;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({
        "batteryCount",
        "message"
})
public class BatteryRegisterResponse {
    private int batteryCount;
    private String message;

    public BatteryRegisterResponse(int batteryCount, String message) {
        this.batteryCount = batteryCount;
        this.message = message;
    }
}
