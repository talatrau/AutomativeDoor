package com.example.automativedoor.EntityClass;

import java.time.LocalDateTime;

public class SpeakerHis extends History {
    public LocalDateTime time;  // alarm time

    public SpeakerHis(String id, String name, LocalDateTime time) {
        super(id, name);
        this.time = time;
    }
}
