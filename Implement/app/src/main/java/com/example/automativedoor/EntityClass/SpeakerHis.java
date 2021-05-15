package com.example.automativedoor.EntityClass;

import java.time.LocalDateTime;

public class SpeakerHis extends History {
    public String time;  // alarm time

    public SpeakerHis(String deviceID, String name) {
        super(deviceID, name);
    }

    public SpeakerHis() {
        super("", "");
    }

    @Override
    public String getStartTime() {
        return null;
    }

    @Override
    public String getEndTime() {
        return null;
    }
}
