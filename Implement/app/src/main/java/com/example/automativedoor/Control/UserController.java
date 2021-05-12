package com.example.automativedoor.Control;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.automativedoor.EntityClass.Account;
import com.example.automativedoor.EntityClass.Sensor;

import com.example.automativedoor.EntityClass.Servo;
import com.example.automativedoor.EntityClass.Speaker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class UserController implements IDatabaseDriver {

    final static public FirebaseAuth fauth = FirebaseAuth.getInstance();
    final static private FirebaseDatabase database = FirebaseDatabase.getInstance();

    static public Account user;
    static public ArrayList<Sensor> sensorList = new ArrayList<Sensor>();
    static public ArrayList<Speaker> speakerList = new ArrayList<Speaker>();
    static public ArrayList<Servo> servoList = new ArrayList<Servo>();

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

    public static boolean setup(String email) {
        String hash = md5Hash(email);
        if (hash == null) {
            return false;
        } else {
            DatabaseReference ref = database.getReference("User").child(hash);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    user = snapshot.getValue(Account.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("some thing wrong with ", error.toString());
                }
            });

            ref = database.getReference("Component").child(hash);
            ref.child("Sensor").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot post : snapshot.getChildren()) {
                        sensorList.add(post.getValue(Sensor.class));
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("some thing wrong with ", error.toString());
                }
            });

            ref.child("Speaker").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot post : snapshot.getChildren()) {
                        speakerList.add(post.getValue(Speaker.class));
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("some thing wrong with ", error.toString());
                }
            });

            ref.child("Servo").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot post : snapshot.getChildren()) {
                        servoList.add(post.getValue(Servo.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("some thing wrong with ", error.toString());
                }
            });

            return true;
        }
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

    private void sendMail() {

    }

    private static String md5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] message = md.digest(input.getBytes());

            BigInteger no = new BigInteger(1, message);
            return no.toString(16);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

}
