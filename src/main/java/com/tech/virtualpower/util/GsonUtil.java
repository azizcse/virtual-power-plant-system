package com.tech.virtualpower.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tech.virtualpower.payload.BatteryDto;

import java.lang.reflect.Type;
import java.util.List;

public class GsonUtil {
    private static GsonUtil gsonUtil;
    private Gson gson;

    private GsonUtil() {
        gson = new Gson();
    }

    public static GsonUtil on() {
        if (gsonUtil == null) {
            gsonUtil = new GsonUtil();
        }
        return gsonUtil;
    }

    public String toStringFromModel(List<BatteryDto> batteryList){
        if (batteryList == null || batteryList.isEmpty())
            return null;
        Type type = new TypeToken<List<BatteryDto>>() {
        }.getType();
        try {
            return gson.toJson(batteryList, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String toStringFromBattery(BatteryDto batteryList){
        if (batteryList == null)
            return null;
        Type type = new TypeToken<BatteryDto>() {
        }.getType();
        try {
            return gson.toJson(batteryList, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<BatteryDto> toModelFromJson(String jsonStr){
        Type type = new TypeToken<List<BatteryDto>>() {
        }.getType();
        try {
            return gson.fromJson(jsonStr, type);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BatteryDto toBatteryFromJson(String jsonStr){
        Type type = new TypeToken<BatteryDto>() {
        }.getType();
        try {
            return gson.fromJson(jsonStr, type);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
