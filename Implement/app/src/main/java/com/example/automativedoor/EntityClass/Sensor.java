package com.example.automativedoor.EntityClass;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.automativedoor.Control.UserController;
import com.google.firebase.database.DatabaseReference;

import java.time.LocalDateTime;

public class Sensor extends Component {

    SensorHis sensorHis;

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
        this.sensorHis.obstacle.add(LocalDateTime.now().toString().substring(0, 19));
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
}
