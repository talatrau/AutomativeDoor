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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HistoryDetail extends AppCompatActivity {
//    Array histories;
//    List<SensorHis> sensorHistories;
//    List<ServoHis> servoHistories;
//    List<SpeakerHis> speakerHistories;

    ArrayList<String[]> servoHistories;
    ArrayList<String[]> speakerHistories;
    ArrayList<String[]> sensorHistories;

    ListView listView;
    HistAdapter histAdapter;

    public ArrayList<String[]> getSensorData(List<SensorHis> sensorHisList){
        ArrayList<String[]> ret = new ArrayList<>();
        SensorHis tempSensor;
        for (int i = 0; i < sensorHisList.size(); i ++){
            tempSensor = sensorHisList.get(i);
            for (int s = 0; s < tempSensor.obstacle.size(); s ++) {
                ret.add(new String[]{tempSensor.deviceID, tempSensor.obstacle.get(s)});
                Log.wtf("Debug", "Warning time: " + tempSensor.obstacle.get(s));
            }
            Log.wtf("Debug", "Device ID: " + tempSensor.deviceID);
//            Log.wtf("Debug", "ret[0]: " + Arrays.toString(ret.get(0)));

        }
        Log.wtf("Debug", "end getServoData");
        return ret;
    }
    public ArrayList<String[]> getServoData(List<ServoHis> servoHisList){
        Log.wtf("Debug", "getServoData ran");
        ArrayList<String[]> ret = new ArrayList<>();
        ServoHis tempServo;
        for (int i = 0; i < servoHisList.size(); i ++){
            tempServo = servoHisList.get(i);
            for (int s = 0; s < tempServo.getSize(); s ++) {
                ret.add(new String[]{tempServo.deviceID, tempServo.getOTime(s), tempServo.getCTime(s)});
                Log.wtf("Debug", "Start time: " + tempServo.getOTime(s));
                Log.wtf("Debug", "End time: " + tempServo.getCTime(s));
            }
            Log.wtf("Debug", "Device ID: " + tempServo.deviceID);
            Log.wtf("Debug", "ret[0]: " + Arrays.toString(ret.get(0)));

        }
        Log.wtf("Debug", "end getServoData");
        return ret;
    }
    public ArrayList<String[]> getSpeakerData(List<SpeakerHis> speakerHisList){
        ArrayList<String[]> ret = new ArrayList<>();
        SpeakerHis tempSpeaker;
        for (int i = 0; i < speakerHisList.size(); i ++){
            tempSpeaker = speakerHisList.get(i);
            for (int s = 0; s < tempSpeaker.time.size(); s ++) {
                ret.add(new String[]{tempSpeaker.deviceID, tempSpeaker.time.get(s)});
                Log.wtf("Debug", "Warning time: " + tempSpeaker.time.get(s));
            }
            Log.wtf("Debug", "Device ID: " + tempSpeaker.deviceID);
            Log.wtf("Debug", "ret[0]: " + Arrays.toString(ret.get(0)));

        }
        Log.wtf("Debug", "end getServoData");
        return ret;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_detail_activity);

//        histories = UserController.getInstance().sensorHisList;
//        HistAdapter histAdapter = new HistAdapter(HistoryDetail.this, R.layout.stream_tworound_history);
//        histAdapter.setListSensorHis(histories);
//
        listView = findViewById(R.id.listHistory);
        if (UserController.getInstance().currentHisType == 0) {
            histAdapter = new HistAdapter(HistoryDetail.this, R.layout.stream_oneround_history);
            sensorHistories = getSensorData(UserController.getInstance().sensorHisList[0]);
            Log.wtf("Debug", "len: " + String.valueOf(sensorHistories.size()));
            histAdapter.setListSensorHis(sensorHistories);
            listView.setAdapter(histAdapter);
        } else if (UserController.getInstance().currentHisType == 1) {
            histAdapter = new HistAdapter(HistoryDetail.this, R.layout.stream_oneround_history);
            speakerHistories = getSpeakerData(UserController.getInstance().speakerHisList[0]);
            histAdapter.setListSpeakerHis(speakerHistories);
            listView.setAdapter(histAdapter);
        } else {
            histAdapter = new HistAdapter(HistoryDetail.this, R.layout.stream_tworound_history);
            servoHistories = getServoData(UserController.getInstance().servoHisList[0]);

            if (servoHistories == null){
                Log.wtf("Debug", "Loi roi");
            }


//            servoHistories = UserController.getInstance().servoHisList[0];
            histAdapter.setListServoHis(servoHistories);
            listView.setAdapter(histAdapter);
        }

//        listView = findViewById(R.id.listHistory);
//        listView.setAdapter(histAdapter);

//        for (int i = 0; i < 7; i ++){
//            if (UserController.getInstance().currentHisType == 2) {
//                if (UserController.getInstance().servoHisList[i] != null){
//                    Log.wtf("Hoang", String.format("servo %d days before: \n%d", i, UserController.getInstance().servoHisList[i].get(0).getSize()));
//                }
//            } else if (UserController.getInstance().currentHisType == 0) {
//                if (UserController.getInstance().sensorHisList[i] != null){
//                    Log.wtf("Hoang", String.format("number sensor: %d", UserController.getInstance().sensorHisList[i].size()));
//                    Log.wtf("Hoang", String.format("sensor %d days before: \n%d", i, UserController.getInstance().sensorHisList[i].get(1).obstacle.size()));
//                }
//            } else{
//                if (UserController.getInstance().speakerHisList[i] != null){
//                    Log.wtf("Hoang", String.format("speaker %d days before: \n%d", i, UserController.getInstance().speakerHisList[i].get(0).time.size()));
//                }
//            }
//        }
    }
//    protected List<SensorHis> getSensorHist(){
//        List<SensorHis> sensorHises = new ArrayList<>();
//        sensorHises.add(new SensorHis("2021-02-ddTHH:mm:ss"))
//    }
}