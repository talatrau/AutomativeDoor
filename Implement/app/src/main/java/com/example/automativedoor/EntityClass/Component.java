package com.example.automativedoor.EntityClass;

public abstract class Component {
    public String deviceID;
    public String name;
    public boolean state;

    public Component(String deviceID, String name, boolean state) {
        this.deviceID = deviceID;
        this.name = name;
        this.state = state;
    }

    public boolean getState() {
        return this.state;
    }

    public abstract void saveHistory();
}
