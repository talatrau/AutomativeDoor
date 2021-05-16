package com.example.automativedoor.Control;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.automativedoor.EntityClass.Account;
import com.example.automativedoor.EntityClass.Response;
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

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class UserController {

    static private UserController instance;
    private DatabaseDriver driver;
    private String hash;
    public List<SensorHis> sensorHisList;
    public List<ServoHis> servoHisList;
    public List<SpeakerHis> speakerHisList;
    public int hisMode; // 0: sensor 1: speaker 2: servo

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

    public void setSpeaker(int position, int volume, MQTTServer mqttServer) {
        Speaker speaker = this.speakerList.get(position);
        speaker.changeVolume(volume);
        this.sendDataMQTT(String.valueOf(volume), "CongTuVu/feeds/automativedoor.buzzer\n", mqttServer);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void turnOnSensor(int position, MQTTServer mqttServer) {
        if (this.sensorList.get(position).toggle(true)) {
            this.sendDataMQTT("ON", "CongTuVu/feeds/automativedoor.sensor\n", mqttServer);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void turnOffSensor(int position, MQTTServer mqttServer) {
        if (this.sensorList.get(position).toggle(false)) {
            this.sendDataMQTT("OFF", "CongTuVu/feeds/automativedoor.sensor\n", mqttServer);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean openDoor(int position, MQTTServer mqttServer) {
        if (this.servoList.get(position).toggle(true)) {
            this.sendDataMQTT("ON", "CongTuVu/feeds/automativedoor.servo\n", mqttServer);
            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean closeDoor(int position, MQTTServer mqttServer) {
        if (this.servoList.get(position).toggle(false)) {
            this.sendDataMQTT("OFF", "CongTuVu/feeds/automativedoor.servo\n", mqttServer);
            return true;
        }
        return false;
    }

    // must start a new thread in this method
    // to always tracking sensor and alarm
    public void trackingSensor() {

    }

    public void sendResponse(int score, String text) {
        Response response = new Response(score, text);
        driver.saveResponse(response);
    }

    public void scheduleSensor(LocalDateTime time) {

    }

    // must start a new thread in this method
    // to always tracking real time to turn on or off sensor
    public void trackingTime() {

    }

    public void loadHistory(int typ, int amount) {
        // typ = 0: sensor 1: speaker 2: servo
        // amount of doccument
        Log.wtf("Load_data", "loadHistory ran");
        driver.getHistory(typ, amount);
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

    private void sendDataMQTT(String data, String feedPath, MQTTServer mqttServer) {
        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(true);

        byte[] b = data.getBytes(Charset.forName("UTF-8"));
        msg.setPayload(b);

        try {
            mqttServer.mqttAndroidClient.publish(feedPath, msg);
            Log.e("taatrau", "ok");
        } catch (Exception e) {
            Log.e("Exception", e.toString());
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
                        Log.wtf("Load data", "done");
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

        public void getHistory(int typ, int amount) {
            if (typ == 0) {
                Log.wtf("Load_data", "type = 0");
                for (Sensor sensor : sensorList) {
                    database.getReference("SensorHis").child(hash).child(sensor.getDeviceID()).limitToLast(amount).addValueEventListener(new ValueEventListener(){
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            sensorHisList = new ArrayList<SensorHis>();
                            for (DataSnapshot post : snapshot.getChildren()) {
                                sensorHisList.add(post.getValue(SensorHis.class));
                            }
                            Log.wtf("Load_data", "size: " + sensorHisList.size());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("some thing wrong with ", error.toString());
                        }
                    });
                }

            } else if (typ == 1){
                for (Speaker speaker : speakerList) {
                    database.getReference("SpeakerHis").child(hash).child(speaker.getDeviceID()).limitToLast(amount).addValueEventListener(new ValueEventListener(){
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            speakerHisList = new ArrayList<SpeakerHis>();
                            for (DataSnapshot post : snapshot.getChildren()) {
                                speakerHisList.add(post.getValue(SpeakerHis.class));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("some thing wrong with ", error.toString());
                        }
                    });
                }
            } else if (typ == 2) {
                for (Servo servo : servoList) {
                    database.getReference("ServoHis").child(hash).child(servo.getDeviceID()).limitToLast(amount).addValueEventListener(new ValueEventListener(){
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            servoHisList = new ArrayList<ServoHis>();
                            for (DataSnapshot post : snapshot.getChildren()) {
                                servoHisList.add(post.getValue(ServoHis.class));
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

        public void saveResponse(Response response) {
            DatabaseReference reference = database.getReference("Response").child(hash).push();
            reference.setValue(response);
        }

    }

}
