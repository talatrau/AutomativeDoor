package com.example.automativedoor.EntityClass;

public abstract class History {
    public String deviceID;
    public String name;

    public History(String deviceID, String name) {
        this.deviceID = deviceID;
        this.name = name;
    }
    public abstract String getHistory();
}
