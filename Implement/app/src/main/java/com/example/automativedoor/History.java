package com.example.automativedoor;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.Property;

import java.util.Calendar;
import java.util.HashMap;

public class History extends AppCompatActivity {
    CustomCalendar customCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_history_activity);

        customCalendar = findViewById(R.id.custome_calendar);

        HashMap<Object, Property> desHashMap = new HashMap<>();
        Property defaultPropety = new Property();
        defaultPropety.layoutResource = R.layout.default_view;
        defaultPropety.dateTextViewResource = R.id.text_view;
        desHashMap.put("default", defaultPropety);

        // for pressed date
        Property currentPropety = new Property();
        currentPropety.layoutResource = R.layout.current_view;
        currentPropety.dateTextViewResource = R.id.text_view;
        desHashMap.put("pressed", currentPropety);

        customCalendar.setMapDescToProp(desHashMap);

        HashMap<Integer, Object> dateHashMap = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        


    }
}
