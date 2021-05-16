package com.example.automativedoor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
import org.naishadhparmar.zcustomcalendar.Property;

import java.util.Calendar;
import java.util.HashMap;
import com.example.automativedoor.Control.UserController;

public class History extends AppCompatActivity {
    Button servoBtn, buzzerBtn, sensorBtn;

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
                UserController.getInstance().hisMode = 2;
                startActivity(new Intent(History.this, GeneralHistory.class));
            }
        });
        sensorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserController.getInstance().hisMode = 0;
                startActivity(new Intent(History.this, GeneralHistory.class));
            }
        });
        buzzerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserController.getInstance().hisMode = 0;
                startActivity(new Intent(History.this, GeneralHistory.class));
            }
        });

    }
}
