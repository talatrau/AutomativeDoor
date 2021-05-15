package com.example.automativedoor;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.automativedoor.Control.UserController;
import com.example.automativedoor.EntityClass.SensorHis;
import com.example.automativedoor.EntityClass.ServoHis;
import com.example.automativedoor.GUIControl.HistAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class HistoryDetail extends AppCompatActivity {
//    Array histories;
    List<SensorHis> histories;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_detail_activity);
        Log.wtf("HistoryDetail", "created");

        Log.wtf("HistoryDetail", "line 29");
        UserController.getInstance().loadHistory(1, 15);
        Log.wtf("HistoryDetail", "line 31");
        histories = UserController.getInstance().sensorHisList;
//        Log.wtf("HistoryDetail", "line 33");
        HistAdapter histAdapter = new HistAdapter(HistoryDetail.this, R.layout.stream_history_detail,  histories);

        listView = findViewById(R.id.listHistory);
        listView.setAdapter(histAdapter);
    }
}