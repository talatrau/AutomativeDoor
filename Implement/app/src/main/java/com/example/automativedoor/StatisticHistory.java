package com.example.automativedoor;

import android.os.Bundle;

import com.example.automativedoor.Control.UserController;
import com.example.automativedoor.GUIControl.ChartAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class StatisticHistory extends AppCompatActivity {
    ListView listChart;

    LineChart tempChart;
    ChartAdapter chartAdapter;
    int[] data_plot = new int[]{4, 2, 3, 4, 2, 0, 5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_history);

        listChart = findViewById(R.id.listChart);

        if (UserController.getInstance().currentHisType == 1) {
            chartAdapter = new ChartAdapter(this, R.layout.stream_line_chart, 1);
            chartAdapter.setSpeaker(UserController.getInstance().speakerHisList, 7);
        } else if (UserController.getInstance().currentHisType == 2) {
            chartAdapter = new ChartAdapter(this, R.layout.stream_servo_chart, 2);
            chartAdapter.setServo(UserController.getInstance().servoHisList, 7);
        } else{
            chartAdapter = new ChartAdapter(this, R.layout.stream_line_chart, 0);
            chartAdapter.setSensor(UserController.getInstance().sensorHisList, 7);
        }

        listChart.setAdapter(chartAdapter);

    }

}