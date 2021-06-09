package com.example.automativedoor.EntityClass;

import java.util.ArrayList;
import java.util.List;

public class ServoHis extends History {
    private List<Time> timeList;

    public String getOTime(int index) {
        return this.timeList.get(index).oTime;
    }

    public String getCTime(int index) {
        return this.timeList.get(index).cTime;
    }

    public void setTime(String oTime, String cTime) {
        Time time = new Time();
        time.oTime = oTime;
        time.cTime = cTime;
        timeList.add(time);
    }

    public ServoHis(String deviceID, String name) {
        super(deviceID, name);
        this.timeList = new ArrayList<>();
    }

    public ServoHis() {
        super();
        this.timeList = new ArrayList<>();
    }

    class Time {
        public String cTime = null;
        public String oTime = null;
    }

}
