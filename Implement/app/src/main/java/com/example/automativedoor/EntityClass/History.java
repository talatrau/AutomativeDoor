package com.example.automativedoor.EntityClass;

import android.util.Log;

public class History {
    public String deviceID;
    public String name;

    public History(String deviceID, String name) {
        this.deviceID = deviceID;
        this.name = name;
    }
    public String getStartTime(){
        Log.wtf("History", "getStart");
        return "history";
    }
    public String getEndTime(){
        Log.wtf("History", "getEndTime");
        return "history";
    }

}
