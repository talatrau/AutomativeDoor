package com.example.automativedoor.EntityClass;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.automativedoor.Control.UserController;
import com.google.firebase.database.DatabaseReference;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Servo extends Component {

    private boolean state;

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean getState() {
        return this.state;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean toggle(boolean signal) {
        if (this.state == signal) {
            return false;
        } else {
            this.state = signal;
            DatabaseReference reference = database.getReference("Component").child(UserController.getInstance().getHash()).child("Servo");
            reference.child(this.currentIndex).setValue(this);
            this.saveHistory();
            return true;
        }
    }

    @Override
    public void updateName(String name) {
        super.updateName(name);
        DatabaseReference reference = database.getReference("Component").child(UserController.getInstance().getHash()).child("Servo");
        reference.child(this.currentIndex).child("name").setValue(name);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void saveHistory() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = formatter.format(LocalDateTime.now());

        if (this.state) {
            this.currentHis += 1;

            String oTime = LocalDateTime.now().toString().substring(0, 19) + " " + UserController.getInstance().getMode();
            database.getReference("ServoHis").child(UserController.getInstance().getHash())
                    .child(this.deviceID).child("time").child(date).child(String.valueOf(this.currentHis)).child("oTime").setValue(oTime);
        }
        else {
            String cTime = LocalDateTime.now().toString().substring(0, 19) + " " + UserController.getInstance().getMode();
            database.getReference("ServoHis").child(UserController.getInstance().getHash())
                    .child(this.deviceID).child("time").child(date).child(String.valueOf(this.currentHis)).child("cTime").setValue(cTime);
        }
    }

}
