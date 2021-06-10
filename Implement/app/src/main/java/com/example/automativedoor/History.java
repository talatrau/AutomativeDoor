package com.example.automativedoor;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.automativedoor.Control.UserController;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class History extends AppCompatActivity {
    Button servoBtn, buzzerBtn, sensorBtn;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_history_activity);

        servoBtn = (Button) findViewById(R.id.servoBtn);
        buzzerBtn = (Button) findViewById(R.id.buzzerBtn);
        sensorBtn = (Button) findViewById(R.id.sensorBtn);

        servoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserController.getInstance().currentHisType = 2;
                Log.wtf("Debug", "History | currentHisType:" + String.valueOf(UserController.getInstance().currentHisType));
                startActivity(new Intent(History.this, GeneralHistory.class));
            }
        });
        buzzerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserController.getInstance().currentHisType = 1;
                Log.wtf("Debug", "History | currentHisType:" + String.valueOf(UserController.getInstance().currentHisType));
                startActivity(new Intent(History.this, GeneralHistory.class));
            }
        });
        sensorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserController.getInstance().currentHisType = 0;
                Log.wtf("Debug", "History | currentHisType:" + String.valueOf(UserController.getInstance().currentHisType));
                startActivity(new Intent(History.this, GeneralHistory.class));
            }
        });

//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        Log.wtf("Debug", "Current time: " + formatter.format(new Date()));

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        Log.wtf("Debug", "Current day: " + today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Log.wtf("Debug", "yesterday: " + yesterday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

    }
}
