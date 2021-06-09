package com.example.automativedoor.EntityClass;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class SensorHis extends History {
    public List<String> obstacle = null;

    public SensorHis() {
        super();
        this.obstacle = new ArrayList<String>();
    }

    public SensorHis(String deviceID, String name) {
        super(deviceID, name);
        this.obstacle = new ArrayList<String>();
    }

}
