package com.example.automativedoor;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.automativedoor.Control.UserController;
import com.example.automativedoor.EntityClass.SensorHis;
import com.example.automativedoor.EntityClass.ServoHis;
import com.example.automativedoor.EntityClass.SpeakerHis;
import com.example.automativedoor.GUIControl.HistAdapter;

import java.util.List;


public class HistoryDetail extends AppCompatActivity {
//    Array histories;
    List<SensorHis> sensorHistories;
    List<ServoHis> servoHistories;
    List<SpeakerHis> speakerHistories;

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_detail_activity);
//        UserController.getInstance().loadHistory(0, 4);

//        histories = UserController.getInstance().sensorHisList;
//        HistAdapter histAdapter = new HistAdapter(HistoryDetail.this, R.layout.stream_tworound_history);
//        histAdapter.setListSensorHis(histories);
//
//        listView = findViewById(R.id.listHistory);
//        listView.setAdapter(histAdapter);
        HistAdapter histAdapter = new HistAdapter(HistoryDetail.this, R.layout.stream_tworound_history);
//        if (UserController.getInstance().currentHisType == 0) {
//            sensorHistories = UserController.getInstance().sensorHisList;
//            Log.wtf("Debug", "len: " + String.valueOf(sensorHistories.size()));
//            histAdapter.setListSensorHis(sensorHistories);
//        } else if (UserController.getInstance().currentHisType == 1) {
//            speakerHistories = UserController.getInstance().speakerHisList;
//            histAdapter.setListSpeakerHis(speakerHistories);
//        } else {
//            servoHistories = UserController.getInstance().servoHisList;
//            histAdapter.setListServoHis(servoHistories);
//        }

        listView = findViewById(R.id.listHistory);
        listView.setAdapter(histAdapter);
    }
//    protected List<SensorHis> getSensorHist(){
//        List<SensorHis> sensorHises = new ArrayList<>();
//        sensorHises.add(new SensorHis("2021-02-ddTHH:mm:ss"))
//    }
}