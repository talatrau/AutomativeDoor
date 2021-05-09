package com.example.automativedoor.EntityClass;

import java.time.LocalDateTime;

public class ServoHis extends History {
    public LocalDateTime cTime;
    public LocalDateTime oTime;

    public ServoHis(String id, String name, LocalDateTime oTime, LocalDateTime cTime) {
        super(id, name);
        this.cTime = cTime;
        this.oTime = oTime;
    }
}
