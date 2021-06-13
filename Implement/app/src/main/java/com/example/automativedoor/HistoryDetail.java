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

//        histories = UserController.getInstance().sensorHisList;
//        HistAdapter histAdapter = new HistAdapter(HistoryDetail.this, R.layout.stream_tworound_history);
//        histAdapter.setListSensorHis(histories);
//
//        listView = findViewById(R.id.listHistory);
//        listView.setAdapter(histAdapter);
//        HistAdapter histAdapter = new HistAdapter(HistoryDetail.this, R.layout.stream_tworound_history);
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

//        listView = findViewById(R.id.listHistory);
//        listView.setAdapter(histAdapter);

        for (int i = 0; i < 7; i ++){
            if (UserController.getInstance().currentHisType == 2) {
                if (UserController.getInstance().servoHisList[i] != null){
                    Log.wtf("Hoang", String.format("servo %d days before: \n%d", i, UserController.getInstance().servoHisList[i].get(0).getSize()));
                }
            } else if (UserController.getInstance().currentHisType == 0) {
                if (UserController.getInstance().sensorHisList[i] != null){
                    Log.wtf("Hoang", String.format("number sensor: %d", UserController.getInstance().sensorHisList[i].size()));
                    Log.wtf("Hoang", String.format("sensor %d days before: \n%d", i, UserController.getInstance().sensorHisList[i].get(1).obstacle.size()));
                }
            } else{
                if (UserController.getInstance().speakerHisList[i] != null){
                    Log.wtf("Hoang", String.format("speaker %d days before: \n%d", i, UserController.getInstance().speakerHisList[i].get(0).time.size()));
                }
            }
        }
    }
//    protected List<SensorHis> getSensorHist(){
//        List<SensorHis> sensorHises = new ArrayList<>();
//        sensorHises.add(new SensorHis("2021-02-ddTHH:mm:ss"))
//    }
}