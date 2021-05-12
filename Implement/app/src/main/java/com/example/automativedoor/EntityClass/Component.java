package com.example.automativedoor.EntityClass;

public abstract class Component {
    protected String deviceID;
    protected String name;
    protected boolean state;

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean getState() {
        return this.state;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceID() {
        return this.deviceID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public abstract void saveHistory();
}
