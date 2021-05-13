package com.example.automativedoor.EntityClass;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.automativedoor.Control.UserController;
import com.google.firebase.database.DatabaseReference;

import java.time.LocalDateTime;

public class Servo extends Component {

    ServoHis servoHis;

    public void setServoHis(ServoHis his) {
        this.currentHis -= 1;
        this.servoHis = his;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean toggle(boolean signal) {
        if (this.state == signal) {
            return false;
        } else {
            this.state = signal;
            DatabaseReference reference = database.getReference("Component").child(UserController.getInstance().getHash()).child("Servo");
            reference.child(this.currentIndex).setValue(this);
            this.genHistory();
            return true;
        }
    }

    @Override
    protected void saveHistory() {
        DatabaseReference reference = database.getReference("ServoHis").child(UserController.getInstance().getHash()).child(this.deviceID).child(String.valueOf(this.currentHis));
        reference.setValue(this.servoHis);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void genHistory() {
        if (this.state) {
            this.currentHis += 1;
            this.servoHis = new ServoHis();
            servoHis.deviceID = this.deviceID;
            servoHis.name = this.name;
            servoHis.oTime = LocalDateTime.now().toString().substring(0, 19);
            this.saveHistory();
        } else {
            this.servoHis.cTime = LocalDateTime.now().toString().substring(0, 19);
            this.saveHistory();
            this.servoHis = null;
        }

    }
}
