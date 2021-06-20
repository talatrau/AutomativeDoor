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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class    Sensor extends Component {

    private int position;
    private int mode;
    private int suspicious = 0;

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return this.position;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void toggle(int mode) {
        this.mode = mode;
        database.getReference("Component").child(UserController.getInstance().getHash()).child("Sensor")
                .child(this.currentIndex).child("mode").setValue(mode);
    }

    @Override
    public void updateName(String name) {
        super.updateName(name);
        DatabaseReference reference = database.getReference("Component").child(UserController.getInstance().getHash()).child("Sensor");
        reference.child(this.currentIndex).child("name").setValue(name);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void saveHistory() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = formatter.format(LocalDateTime.now());
        this.currentHis += 1;

        String time = LocalDateTime.now().toString().substring(0, 19) + " " + UserController.getInstance().getMode();
        database.getReference("SensorHis").child(UserController.getInstance().getHash()).child(this.deviceID)
                .child("obstacle").child(date).child(String.valueOf(this.currentHis)).setValue(time);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void processConnect(String data) {
        if (this.mode == 2) {
            if (!data.equals("0")) {
                if (this.position == 1) UserController.getInstance().alarmSpeaker(true);
                else if (this.position == 0) {
                    this.suspicious += 1;
                    if (this.suspicious > 5) {
                        this.suspicious = 0;
                        UserController.getInstance().alarmSpeaker(true);
                    }
                }
                this.saveHistory();
            }
            else {
                this.suspicious = 0;
                UserController.getInstance().alarmSpeaker(false);
            }
        }
        else if (this.mode == 1) {
            Log.e("in process", "connect");
            if (data.equals("1")) {
                this.saveHistory();
                UserController.getInstance().openDoor();
                Log.e("data equal", "one");
            }
        }
    }
}
