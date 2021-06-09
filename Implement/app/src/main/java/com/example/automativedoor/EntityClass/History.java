package com.example.automativedoor.EntityClass;

import android.util.Log;

public class History {
    public String deviceID;
    public String name;

    public History() {
        this.deviceID = "";
        this.name = "";
    }

    public History(String deviceID, String name) {
        this.deviceID = deviceID;
        this.name = name;
    }

}
