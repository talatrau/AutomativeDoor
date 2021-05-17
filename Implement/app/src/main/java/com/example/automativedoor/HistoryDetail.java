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
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_detail_activity);

        Log.wtf("HistoryDetail", "ran");

        HistAdapter histAdapter = new HistAdapter(HistoryDetail.this, R.layout.stream_tworound_history);
        if (UserController.getInstance().hisMode == 0) {
            Log.wtf("HistotyDetail", "sensor histories");
            histAdapter.setListSensorHis(UserController.getInstance().sensorHisList);
        }
        else if (UserController.getInstance().hisMode == 1) {
            Log.wtf("HistotyDetail", "speaker histories");
            histAdapter.setListSpeakerHis(UserController.getInstance().speakerHisList);
        }
        else if (UserController.getInstance().hisMode == 2) {
            Log.wtf("HistotyDetail", "servo histories");
            histAdapter.setListServoHis(UserController.getInstance().servoHisList);
        }

        listView = findViewById(R.id.listHistory);
        listView.setAdapter(histAdapter);
    }
//    protected List<SensorHis> getSensorHist(){
//        List<SensorHis> sensorHises = new ArrayList<>();
//        sensorHises.add(new SensorHis("2021-02-ddTHH:mm:ss"))
//    }
}