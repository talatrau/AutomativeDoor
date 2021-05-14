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
        // TODO: set real buzzer volume

        this.setVolume(volume);
        DatabaseReference reference = database.getReference("Component").child(UserController.getInstance().getHash()).child("Speaker");
        reference.child(this.currentIndex).setValue(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void alarm(int second) {
        // TODO: alarm real buzzer



        this.saveHistory();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void saveHistory() {
        Log.e("run", "ok");
        this.speakerHis = new SpeakerHis();
        speakerHis.deviceID = this.deviceID;
        speakerHis.name = this.name;
        speakerHis.time = LocalDateTime.now().toString().substring(0, 19);
        DatabaseReference reference = database.getReference("SpeakerHis").child(UserController.getInstance().getHash()).child(this.deviceID).child(String.valueOf(this.currentHis));
        reference.setValue(this.speakerHis);
    }
}
