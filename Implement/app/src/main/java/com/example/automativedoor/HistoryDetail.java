package com.example.automativedoor;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.automativedoor.Control.UserController;
import com.example.automativedoor.EntityClass.SensorHis;
import com.example.automativedoor.GUIControl.HistAdapter;

import java.util.List;


public class HistoryDetail extends AppCompatActivity {
//    Array histories;
    List<SensorHis> histories;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_detail_activity);
//        UserController.getInstance().loadHistory(0, 4);

        histories = UserController.getInstance().sensorHisList;
        HistAdapter histAdapter = new HistAdapter(HistoryDetail.this, R.layout.stream_tworound_history);
        histAdapter.setListSensorHis(histories);

        listView = findViewById(R.id.listHistory);
        listView.setAdapter(histAdapter);
    }
//    protected List<SensorHis> getSensorHist(){
//        List<SensorHis> sensorHises = new ArrayList<>();
//        sensorHises.add(new SensorHis("2021-02-ddTHH:mm:ss"))
//    }
}