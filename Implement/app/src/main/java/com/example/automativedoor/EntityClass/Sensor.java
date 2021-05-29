package com.example.automativedoor.EntityClass;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.automativedoor.Control.SensorService;
import com.example.automativedoor.Control.UserController;
import com.google.firebase.database.DatabaseReference;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Sensor extends Component {

    SensorHis sensorHis;
    private String connectID;
    private int connectIndex;

    private CountDownTimer counter;

    public void setConnectID(String device) {
        this.connectID = device;
        if (connectID.contains("speaker")) connectIndex = UserController.getInstance().getSpeakerIndex(device);
        else if (connectID.contains("servo")) {
            connectIndex = UserController.getInstance().getServoIndex(device);
            this.counter = new CountDownTimer(5000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) { }

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onFinish() {
                    Log.e("counter", "finished");
                    UserController.getInstance().closeDoor(connectIndex);
                }
            };
        }
    }

    public String getConnectID() {
        return this.connectID;
    }

    public void setSensorHis(SensorHis his) {
        this.sensorHis = his;
        this.currentHis -= 1;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean toggle(boolean signal) {
        if (this.state == signal) {
            return false;
        } else {
            this.state = signal;
            this.genHistory();
            UserController.getInstance().setMqttSubcribe(this.mqttTopic, signal);
            return true;
        }
    }

    @Override
    protected void saveHistory() {
        DatabaseReference reference = database.getReference("SensorHis").child(UserController.getInstance().getHash()).child(this.deviceID).child(String.valueOf(this.currentHis));
        reference.setValue(this.sensorHis);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveObstacle() {
        if (this.sensorHis.obstacle == null) {
            this.sensorHis.obstacle = new ArrayList<String>();
        }
        this.sensorHis.obstacle.add(LocalDateTime.now().toString().substring(0, 19));
        this.saveHistory();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void genHistory() {
        DatabaseReference reference = database.getReference("Component").child(UserController.getInstance().getHash()).child("Sensor");
        reference.child(this.currentIndex).setValue(this);
        if (this.state) {
            this.currentHis += 1;
            this.sensorHis = new SensorHis();
            sensorHis.deviceID = this.deviceID;
            sensorHis.name = this.name;
            sensorHis.sTime = LocalDateTime.now().toString().substring(0, 19);
            this.saveHistory();
        } else {
            this.sensorHis.eTime = LocalDateTime.now().toString().substring(0, 19);
            this.saveHistory();
            this.sensorHis = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void processConnect(String data) {
        if (this.connectID.contains("speaker")) {
            if (!data.equals("00")) {
                this.saveObstacle();
                UserController.getInstance().alarmSpeaker(true, this.connectIndex);
            }
            else {
                UserController.getInstance().alarmSpeaker(false, this.connectIndex);
            }
        }
        else if (this.connectID.contains("servo")) {
            if (!data.equals("00")) {
                this.saveObstacle();
                UserController.getInstance().openDoor(this.connectIndex);
                this.counter.cancel();
            }
            else {
                this.counter.start();
            }
        }
    }
}
