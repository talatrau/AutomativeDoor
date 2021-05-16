package com.example.automativedoor;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.automativedoor.Control.UserController;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
import org.naishadhparmar.zcustomcalendar.Property;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;

public class GeneralHistory extends AppCompatActivity {
    CalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.wtf("GeneralHistory", "created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.working_history_activity);

        UserController.getInstance().loadHistory(UserController.getInstance().hisMode, 100);

        calendar = (CalendarView) findViewById(R.id.calendarHistory);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Log.wtf("GeneralHitory", "onSelectedChange");
                LocalDate date = LocalDate.of(year, month+1, dayOfMonth);
                Toast.makeText(GeneralHistory.this, date.toString(), Toast.LENGTH_LONG).show();
                Log.wtf("GeneralHistory", "start new intent");
                startActivity(new Intent(GeneralHistory.this, HistoryDetail.class));
            }
        });



    }
}
