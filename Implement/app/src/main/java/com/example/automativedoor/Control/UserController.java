package com.example.automativedoor.Control;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.time.LocalDateTime;

public class UserController implements IDatabaseDriver {

    private final static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    public void setSpeaker() {

    }

    public boolean turnOnSensor() {
        return true;
    }

    public boolean turnOffSensor() {
        return true;
    }

    public boolean openDoor() {
        return true;
    }

    public boolean closeDoor() {
        return true;
    }

    // must start a new thread in this method
    // to always tracking sensor and alarm
    public void trackingSensor() {

    }

    public void sendResponse() {

    }

    public void scheduleSensor(LocalDateTime time) {

    }

    // must start a new thread in this method
    // to always tracking real time to turn on or off sensor
    public void trackingTime() {

    }

    public void viewHistory() {

    }

    private void sendMail() {

    }


    public static boolean login(String email, String pass) {
        if (firebaseAuth.getCurrentUser() != null) {
            return true;
        } else if (email.isEmpty()) {
            return false;
        } else {

            final boolean[] result = new boolean[1];
            firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    result[0] = task.isSuccessful();
                }
            });

            return result[0];
        }
    }

    public static void logout() {
        firebaseAuth.signOut();
    }

    @Override
    public void writeJson() {

    }

    @Override
    public String readJson() {
        return null;
    }

    @Override
    public void writeDatabase() {

    }

    @Override
    public String readDatabase() {
        return null;
    }
}
