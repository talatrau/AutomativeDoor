package com.example.automativedoor.EntityClass;

import java.time.LocalDateTime;

public class ServoHis extends History {
    public String cTime = "";
    public String oTime = "";

    public ServoHis(String deviceID, String name) {
        super(deviceID, name);
    }

    public ServoHis() {
        super("1", "");
    }

}
