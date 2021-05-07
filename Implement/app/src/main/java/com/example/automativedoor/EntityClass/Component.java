package com.example.automativedoor.EntityClass;

public abstract class Component {
    public String deviceID;
    public String name;

    public Component(String deviceID, String name) {
        this.deviceID = deviceID;
        this.name = name;
    }

    public abstract void saveHistory();
}
