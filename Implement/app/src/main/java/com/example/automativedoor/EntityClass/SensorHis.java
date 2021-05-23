package com.example.automativedoor.EntityClass;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class SensorHis extends History {
    public String sTime = "";
    public String eTime = "";
    public List<String> obstacle;

    public SensorHis() {
        super("1", "no");
    }
    public SensorHis(String sTime, String eTime) {
        super("1", "no");
        this.sTime = sTime;
        this.eTime = eTime;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public String timeDistance() {
        String distance = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddTHH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(this.sTime, formatter);
        LocalDateTime end = LocalDateTime.parse(this.eTime, formatter);

        LocalDateTime fromTemp = LocalDateTime.from(start);
        long years = fromTemp.until(end, ChronoUnit.YEARS);
        fromTemp = fromTemp.plusYears(years);
        if (years > 0) distance += String.valueOf(years) + " years ";

        long months = fromTemp.until(end, ChronoUnit.MONTHS);
        fromTemp = fromTemp.plusMonths(months);
        if (months > 0) distance += String.valueOf(months) + " months ";

        long days = fromTemp.until(end, ChronoUnit.DAYS);
        fromTemp = fromTemp.plusDays(days);
        if (days > 0) distance += String.valueOf(days) + " days ";

        long hours = fromTemp.until(end, ChronoUnit.HOURS);
        fromTemp = fromTemp.plusHours(hours);
        if (hours > 0) distance += String.valueOf(hours) + " hours ";

        long minutes = fromTemp.until(end, ChronoUnit.MINUTES);
        fromTemp = fromTemp.plusMinutes(minutes);
        if (minutes > 0) distance += String.valueOf(minutes) + " minutes ";

        long seconds = fromTemp.until(end, ChronoUnit.SECONDS);
        distance += String.valueOf(seconds) + " senconds";

        return distance;        // return distance between start and end time
    }

}
