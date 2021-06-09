package com.example.automativedoor.EntityClass;

import com.google.firebase.database.FirebaseDatabase;

public abstract class Component {
    protected String deviceID;
    protected String name;
    protected int currentHis;
    protected String currentIndex;
    protected FirebaseDatabase database;

    public void setDatabase(FirebaseDatabase database) { this.database = database; }

    public void setCurrentIndex(String index) { this.currentIndex = index; }

    public void setCurrentHis(int index) { this.currentHis = index; }

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

    protected void updateName(String name) {
        this.name = name;
    }

    protected abstract void saveHistory();
}
