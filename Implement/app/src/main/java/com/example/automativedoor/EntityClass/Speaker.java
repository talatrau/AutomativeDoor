package com.example.automativedoor.EntityClass;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.automativedoor.Control.UserController;
import com.google.firebase.database.DatabaseReference;

import java.time.LocalDateTime;

public class Speaker extends Component {

    SpeakerHis speakerHis;

    private int volume;

    public int getVolume() {
        return this.volume;
    }

    public void setVolume(int volume) { this.volume = volume; }

    public void changeVolume(int volume) {
        this.setVolume(volume);
        DatabaseReference reference = database.getReference("Component").child(UserController.getInstance().getHash()).child("Speaker");
        reference.child(this.currentIndex).setValue(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String alarm(boolean signal) {
        if (signal) {
            String message = "{\n" +
                    "\"id\":\"3\",\n" +
                    "\"name\":\"SPEAKER\",\n" +
                    "\"data\":\"" + String.valueOf(this.volume * 10) + "\",\n" +
                    "\"unit\":\"\"\n" +
                    "}\n";
            this.saveHistory();
            return message;
        }
        else {
            String message = "{\n" +
                    "\"id\":\"3\",\n" +
                    "\"name\":\"SPEAKER\",\n" +
                    "\"data\":\"0\",\n" +
                    "\"unit\":\"\"\n" +
                    "}\n";
            return message;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void saveHistory() {
        this.currentHis += 1;
        if (this.currentHis == 0) {
            database.getReference("SpeakerHis").child(UserController.getInstance()
                    .getHash()).child(this.deviceID).child("deviceID").setValue(this.deviceID);
            database.getReference("SpeakerHis").child(UserController.getInstance()
                    .getHash()).child(this.deviceID).child("name").setValue(this.name);
        }


        String time = LocalDateTime.now().toString().substring(0, 19);
        database.getReference("SpeakerHis").child(UserController.getInstance()
                .getHash()).child(this.deviceID).child("time").child(String.valueOf(this.currentHis)).setValue(time);
    }

    @Override
    public void updateName(String name) {
        super.updateName(name);
        DatabaseReference reference = database.getReference("Component").child(UserController.getInstance().getHash()).child("Speaker");
        reference.child(this.currentIndex).child("name").setValue(name);
    }
}
