package com.example.automativedoor.EntityClass;

import java.util.ArrayList;
import java.util.List;

public class SpeakerHis extends History {
    public List<String> time = null;

    public SpeakerHis(String deviceID, String name) {
        super(deviceID, name);
        this.time = new ArrayList<String>();
    }

    public SpeakerHis() {
        super();
        this.time = new ArrayList<String>();
    }

}
