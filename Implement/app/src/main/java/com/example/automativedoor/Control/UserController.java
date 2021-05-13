package com.example.automativedoor.Control;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.automativedoor.EntityClass.Account;
import com.example.automativedoor.EntityClass.Component;
import com.example.automativedoor.EntityClass.History;
import com.example.automativedoor.EntityClass.Sensor;

import com.example.automativedoor.EntityClass.SensorHis;
import com.example.automativedoor.EntityClass.Servo;
import com.example.automativedoor.EntityClass.ServoHis;
import com.example.automativedoor.EntityClass.Speaker;
import com.example.automativedoor.EntityClass.SpeakerHis;
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


public class UserController {

    static private UserController instance;

    private DatabaseDriver driver;

    private String hash;

    final public FirebaseAuth fauth = FirebaseAuth.getInstance();

    public Account user;
    public ArrayList<Sensor> sensorList;
    public ArrayList<Speaker> speakerList;
    public ArrayList<Servo> servoList;

    public static UserController getInstance() {
        if (instance == null) {
            instance =  new UserController();
            return instance;
        }
        return instance;
    }

    private UserController() {
        driver = new DatabaseDriver();
    }

    public String getHash() {
        return this.hash;
    }

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

    public boolean setup() {
        this.hash = md5Hash(fauth.getCurrentUser().getEmail());
        if (hash == null) {
            return false;
        } else {
            driver.readComponent();
            driver.readUser();
            return true;
        }
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


    class DatabaseDriver {
        final private FirebaseDatabase database = FirebaseDatabase.getInstance();
        private DatabaseReference ref;

        public void readComponent() {
            ref = database.getReference("Component").child(hash);
            ref.child("Sensor").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    sensorList = new ArrayList<Sensor>();
                    for (DataSnapshot post : snapshot.getChildren()) {
                        Sensor sensor = post.getValue(Sensor.class);
                        sensor.setCurrentIndex(post.getKey());
                        sensor.setDatabase(database);
                        sensorList.add(sensor);
                        driver.getCurrentHis(0, sensor.getDeviceID(), sensorList.size() - 1);
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
                    speakerList = new ArrayList<Speaker>();
                    for (DataSnapshot post : snapshot.getChildren()) {
                        Speaker speaker = post.getValue(Speaker.class);
                        speaker.setCurrentIndex(post.getKey());
                        speaker.setDatabase(database);
                        speakerList.add(speaker);
                        driver.getCurrentHis(1, speaker.getDeviceID(), speakerList.size() - 1);
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
                    servoList = new ArrayList<Servo>();
                    for (DataSnapshot post : snapshot.getChildren()) {
                        Servo servo = post.getValue(Servo.class);
                        servo.setCurrentIndex(post.getKey());
                        servo.setDatabase(database);
                        servoList.add(servo);
                        driver.getCurrentHis(2, servo.getDeviceID(), servoList.size() - 1);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("some thing wrong with ", error.toString());
                }
            });

        }

        public void readUser() {
            ref = database.getReference("User").child(hash);
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
        }

        public void getCurrentHis(int typ, String deviceID, int index) {
            if (typ == 0) {
                database.getReference("SensorHis").child(hash).child(deviceID).limitToLast(1).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        sensorList.get(index).setCurrentHis(-1);
                        for (DataSnapshot post : snapshot.getChildren()) {
                            sensorList.get(index).setCurrentHis(Integer.parseInt(post.getKey()) + 1);
                            sensorList.get(index).setSensorHis(post.getValue(SensorHis.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("some thing wrong with ", error.toString());
                    }
                });

            } else if (typ == 1) {
                database.getReference("SpeakerHis").child(hash).child(deviceID).limitToLast(1).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        speakerList.get(index).setCurrentHis(0);
                        for (DataSnapshot post : snapshot.getChildren()) {
                            speakerList.get(index).setCurrentHis(Integer.parseInt(post.getKey()) + 1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("some thing wrong with ", error.toString());
                    }
                });

            } else if (typ == 2) {
                database.getReference("ServoHis").child(hash).child(deviceID).limitToLast(1).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        servoList.get(index).setCurrentHis(-1);
                        for (DataSnapshot post : snapshot.getChildren()) {
                            servoList.get(index).setCurrentHis(Integer.parseInt(post.getKey()) + 1);
                            servoList.get(index).setServoHis(post.getValue(ServoHis.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("some thing wrong with ", error.toString());
                    }
                });

            }
        }

    }

}
