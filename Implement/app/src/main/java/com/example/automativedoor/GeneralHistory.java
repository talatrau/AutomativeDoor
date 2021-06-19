package com.example.automativedoor;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.automativedoor.Control.UserController;

import java.time.LocalDate;
import java.util.Timer;
import java.util.TimerTask;


public class GeneralHistory extends AppCompatActivity {
    CalendarView calendar;
    Button statisticalBtn;
    Button showDetailBtn;
    LocalDate date;
    Timer timer;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {    
        Log.wtf("GeneralHistory", "created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.working_history_activity);
        timer = new Timer();


        UserController.getInstance().loadHistory(UserController.getInstance().currentHisType, LocalDate.now().toString(), 7);
//        try {
//            UserController.getInstance().latch.await();
//            Log.wtf("Hoang", "Load 7 days done");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        UserController.getInstance().loadHistory(UserController.getInstance().currentHisType, "");

        Log.wtf("Debug", String.format("GeneralHistory | currentHistype: %d", UserController.getInstance().currentHisType));
        calendar = (CalendarView) findViewById(R.id.calendarHistory);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                date = LocalDate.of(year, month+1, dayOfMonth);
                Toast.makeText(GeneralHistory.this, date.toString(), Toast.LENGTH_LONG).show();
                UserController.getInstance().loadHistory(UserController.getInstance().currentHisType, date.toString(), 1);
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(GeneralHistory.this, GeneralHistory.class);
                        startActivity(intent);
                        finish();
                    }
                }, 6000);
//                try {
//                    UserController.getInstance().latch.await();
//                    Log.wtf("Hoang", "Latch Completed");
////                    startActivity(new Intent(GeneralHistory.this, HistoryDetail.class));
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        });

        statisticalBtn = findViewById(R.id.statisticalBtn);
        statisticalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GeneralHistory.this, StatisticHistory.class));
            }
        });

        showDetailBtn =  findViewById(R.id.showDetailBtn);
        showDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GeneralHistory.this, HistoryDetail.class));
            }
        });



    }
}
