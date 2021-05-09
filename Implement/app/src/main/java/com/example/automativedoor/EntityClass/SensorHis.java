package com.example.automativedoor.EntityClass;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class SensorHis extends History {
    public LocalDateTime sTime;
    public LocalDateTime eTime;
    public String period;
    public List<String> obstacle;

    public SensorHis(String id, String name, LocalDateTime sTime, LocalDateTime eTime, List<String> obstacle) {
        super(id, name);
        this.sTime = sTime;
        this.eTime = eTime;
        this.obstacle = obstacle;
        this.distance();
    }

    private void distance() {
        this.period = "";
        LocalDateTime temp = LocalDateTime.from(this.sTime);

        long years = temp.until(this.eTime, ChronoUnit.YEARS);
        if (years > 0) this.period += years + " years ";
        temp = temp.plusYears(years);

        long months = temp.until(this.eTime, ChronoUnit.MONTHS);
        if (months > 0) this.period += months + " months ";
        temp = temp.plusMonths(months);

        long days = temp.until(this.eTime, ChronoUnit.DAYS);
        if (days > 0) this.period += days + " days ";
        temp = temp.plusDays(days);

        long hours = temp.until(this.eTime, ChronoUnit.HOURS);
        if (hours > 0) this.period += hours + " hours ";
        temp = temp.plusHours(hours);

        long minutes = temp.until(this.eTime, ChronoUnit.MINUTES);
        if (minutes > 0) this.period += minutes + " minutes ";
        temp = temp.plusMinutes(minutes);

        long seconds = temp.until(this.eTime, ChronoUnit.SECONDS);
        this.period += seconds + " seconds";

    }
}
