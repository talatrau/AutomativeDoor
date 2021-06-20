package com.example.automativedoor.Control;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.automativedoor.EntityClass.Account;
import com.example.automativedoor.EntityClass.Response;
import com.example.automativedoor.EntityClass.Sensor;

import com.example.automativedoor.EntityClass.SensorHis;
import com.example.automativedoor.EntityClass.Servo;
import com.example.automativedoor.EntityClass.ServoHis;
import com.example.automativedoor.EntityClass.Speaker;
import com.example.automativedoor.EntityClass.SpeakerHis;
import com.example.automativedoor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;


import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class UserController {

    static private UserController instance;
    private DatabaseDriver driver;
    private String hash;
    private MQTTServer mqttServer_1;
    private MQTTServer mqttServer_2;
    private MQTTServer mqttServer_3;
    private int serviceCount = 0;
    private int mode = 0;
    private CountDownTimer closeDoorCount = new CountDownTimer(10000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onFinish() {
            Log.e("counter", "finish");
            closeDoor();
        }
    };

    public List<SensorHis>[] sensorHisList;
    public List<ServoHis>[] servoHisList;
    public List<SpeakerHis>[] speakerHisList;

    public int[][] servoModeCount;

    final public FirebaseAuth fauth = FirebaseAuth.getInstance();

    public Context context;
    public Account user;
    public ArrayList<Sensor> sensorList;
    public ArrayList<Speaker> speakerList;
    public ArrayList<Servo> servoList;
    public CountDownLatch latch;
    public int currentHisType; // 0: sensor 1: speaker 2: servo
    public int numberSpeaker = 1;
    public int numberSensor = 2;
    public int numberServo = 1;

    private UserController() {
        this.driver = new DatabaseDriver();
    }

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
            return instance;
        }
        return instance;
    }

    public int getMode() {
        return this.mode;
    }

    public void closeMqttConnection() {
        if (this.mode == 1 || true) {
            try {
                this.mqttServer_1.mqttAndroidClient.disconnect();
                this.mqttServer_1 = null;
                this.mqttServer_3.mqttAndroidClient.disconnect();
                this.mqttServer_3 = null;
                this.mqttServer_2.mqttAndroidClient.disconnect();
                this.mqttServer_2 = null;
                this.context.stopService(new Intent(context, SensorService.class));
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public void setMqttServer() {
        boolean flag = false;
        if (this.mqttServer_2 == null) {
            this.mqttServer_2 = new MQTTServer(context, "MqttService1", "CongTuVu", "", "CongTuVu/feeds/automativedoor.sensorspeaker");
            flag = true;
            this.trackingSensor(0);
        }
        if (this.mqttServer_1 == null) {
            this.mqttServer_1 = new MQTTServer(context, "MqttService", "CongTuVu", "", "");
        }
        if (this.mqttServer_3 == null) {
            this.mqttServer_3 = new MQTTServer(context, "MqttService2", "CongTuVu", "", "CongTuVu/feeds/automativedoor.sensorservo");
            flag = true;
            this.trackingSensor(1);
        }
        if (flag) this.context.startService(new Intent(context, SensorService.class));
        Log.e("mqtt connection", "connected");
    }

    public String getHash() {
        return this.hash;
    }

    public void setSpeaker(int position, int volume) {
        Speaker speaker = this.speakerList.get(position);
        speaker.changeVolume(volume);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void alarmSpeaker(boolean signal) {
        String mess = this.speakerList.get(0).alarm(signal);
        String topic = "CongTuVu/feeds/automativedoor.buzzer";
        this.mqttServer_1.publishMqtt(mess, topic);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void changeMode(int mode) {
        if (this.mode != mode) {
            this.mode = mode;
            for (int i = 0; i < sensorList.size(); i++) {
                sensorList.get(i).toggle(mode);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean openDoor() {
        if (this.servoList.get(0).toggle(true)) {
            closeDoorCount.cancel();
            String topic = "CongTuVu/feeds/automativedoor.servo";
            this.mqttServer_2.publishMqtt("{\n" +
                    "\"id\":\"17\",\n" +
                    "\"name\":\"SERVO\",\n" +
                    "\"data\":\"180\",\n" +
                    "\"unit\":\"degree\"\n" +
                    "}\n", topic);
            closeDoorCount.start();
            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean closeDoor() {
        if (this.servoList.get(0).toggle(false)) {
            String topic = "CongTuVu/feeds/automativedoor.servo";
            this.mqttServer_2.publishMqtt("{\n" +
                    "\"id\":\"17\",\n" +
                    "\"name\":\"SERVO\",\n" +
                    "\"data\":\"0\",\n" +
                    "\"unit\":\"degree\"\n" +
                    "}\n", topic);
            return true;
        }
        return false;
    }

    public void trackingSensor(int position) {
        if (position == 0) {
            mqttServer_2.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    Log.w("mqtt", serverURI);
                }

                @Override
                public void connectionLost(Throwable cause) {
                    Log.w("Lost mqtt 2", "connection");
                }

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    JSONObject jsonObject = new JSONObject(message.toString());
                    String data = jsonObject.getString("data");
                    for (int i = 0; i < sensorList.size(); i++) {
                        if (sensorList.get(i).getPosition() == 0) {
                            sensorList.get(i).processConnect(data);
                        }
                    }
                    Log.w("message arrived 2", message.toString());
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });
        }
        else if (position == 1) {
            mqttServer_3.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    Log.w("mqtt", serverURI);
                }

                @Override
                public void connectionLost(Throwable cause) {
                    Log.w("Lost mqtt 3", "connection");
                }

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Log.w("message arrived 3", message.toString());
                    JSONObject jsonObject = new JSONObject(message.toString());
                    String data = jsonObject.getString("data");
                    for (int i = 0; i < sensorList.size(); i++) {
                        if (sensorList.get(i).getPosition() == 1) {
                            Log.e("found sensor", i + "");
                            sensorList.get(i).processConnect(data);
                        }
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });
        }

    }

    public JSONArray readJson(String file) {
        byte[] data = driver.readFile(file);
        String json = new String(data);

        try {
            JSONArray result = new JSONArray(json);
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public void writeJson(JSONArray data, String file) {
        driver.writeFile(data.toString().getBytes(), file);
    }

    public boolean sendResponse(int score, String text, String category) {
        String data = "";
        String[] tokens = text.toLowerCase().split("[^a-zA-Z]+");
        if (tokens.length > 0) {
            InputStream inputStream = context.getResources().openRawResource(R.raw.spam);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            if (inputStream != null) {
                try {
                    double spamProb = 1;
                    double hamProb = 1;
                    while ((data = reader.readLine()) != null) {
                        String[] split = data.split("\t");
                        String word = split[0];
                        for (int i = 0; i < tokens.length; i++) {
                            if (tokens[i].equals(word)) {
                                double spam = Double.parseDouble(split[2]) * 10000;
                                double ham = Double.parseDouble(split[1]) * 10000;
                                spamProb = spamProb * spam;
                                hamProb = hamProb * ham;
                            }
                        }
                    }
                    spamProb = spamProb * 0.3;
                    hamProb = hamProb * 0.7;
                    if (hamProb > spamProb) {
                        Response response = new Response(score, text, user.getEmail());
                        driver.saveResponse(response, category);
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("cannot open", "spam.txt");
            }
        }

        return false;
    }


    public void loadHistory(int typ, String date, int num) {
        if (typ == 0) {
            sensorHisList = new ArrayList[num];
        }
        else if (typ == 1) {
            speakerHisList = new ArrayList[num];
        }
        else if (typ == 2) {
            servoHisList = new ArrayList[num];
        }
        latch = new CountDownLatch(num);
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
//                latch = new CountDownLatch(num);
                LocalDate pivot = LocalDate.parse(date);
                for (int i = 0; i < num; i++) {
                    if (typ == 0){
                        sensorHisList[i] = null;
                    } else if (typ == 1){
                        speakerHisList[i] = null;
                    } else{
                        servoHisList[i] = null;
                    }
                    String oldDate = pivot.minusDays(i).toString();
                    driver.getHistory(typ, oldDate, i);
                }
            }
        });
    }


    public DatabaseReference setup() {
        this.hash = md5Hash(fauth.getCurrentUser().getEmail());
        driver.readComponent();
        return driver.readUser();
    }


    public void updateUser() {
        driver.writeUser();
    }


    public void forgotPass(String email, String pin) {
        this.hash = md5Hash(email);
        driver.readUser().get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (user != null && user.pinVerify(pin)) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(context, "Check your email to reset password", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else {
                    Toast.makeText(context, "Invalid email or PIN", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void sendMail(String subject, String content) {
        final String username = "smarthome.hcmut.k18@gmail.com";
        final String password = "54066034a";
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setContent(content, "text/html");
            message.setSubject(subject);

            new SendMail(message).execute();

        } catch (MessagingException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }

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
                @RequiresApi(api = Build.VERSION_CODES.O)
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
                    mode = sensorList.get(0).getMode();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("some thing wrong with ", error.toString());
                }
            });

            ref.child("Speaker").addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
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
                @RequiresApi(api = Build.VERSION_CODES.O)
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

        public DatabaseReference readUser() {
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
            return ref;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void getCurrentHis(int typ, String deviceID, int index) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String date = formatter.format(LocalDateTime.now());
            if (typ == 0) {
                database.getReference("SensorHis").child(hash).child(deviceID).child("obstacle").child(date).limitToLast(1).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            DataSnapshot snapshot = task.getResult();
                            int hisIndex = -1;
                            for (DataSnapshot post : snapshot.getChildren()) {
                                hisIndex = Integer.parseInt(post.getKey());
                            }
                            sensorList.get(index).setCurrentHis(hisIndex);
                        }
                    }
                });

            } else if (typ == 1) {
                database.getReference("SpeakerHis").child(hash).child(deviceID).child("time").child(date).limitToLast(1).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            DataSnapshot snapshot = task.getResult();
                            int hisIndex = -1;
                            for (DataSnapshot post : snapshot.getChildren()) {
                                hisIndex = Integer.parseInt(post.getKey());
                            }
                            speakerList.get(index).setCurrentHis(hisIndex);
                        }
                    }
                });

            } else if (typ == 2) {
                database.getReference("ServoHis").child(hash).child(deviceID).child("time").child(date).limitToLast(1).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            DataSnapshot snapshot = task.getResult();
                            int hisIndex = -1;
                            for (DataSnapshot post : snapshot.getChildren()) {
                                hisIndex = Integer.parseInt(post.getKey());
                            }
                            servoList.get(index).setCurrentHis(hisIndex);
                        }
                    }
                });
            }
        }

        public void getHistory(int typ, String date, int index) {
            DatabaseReference reference;
            if(typ == 0) {
                sensorHisList[index] = new ArrayList<>();
                for (Sensor sensor : sensorList) {
                    SensorHis history = new SensorHis(sensor.getDeviceID(), sensor.getName());
                    reference = database.getReference("SensorHis").child(hash).child(sensor.getDeviceID()).child("obstacle").child(date);

                    reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                List<String> obstacle = new ArrayList<>();
                                DataSnapshot snapshot = task.getResult();
                                for (DataSnapshot post : snapshot.getChildren()) {
                                    obstacle.add(post.getValue().toString());
                                }
                                history.obstacle = obstacle;
                                sensorHisList[index].add(history);

                            }
                            Log.wtf("Hoang", "onCompleted ran");
                            latch.countDown();
                        }
                    });
                }

            } else if(typ ==1) {
                speakerHisList[index] = new ArrayList<>();
                for (Speaker speaker : speakerList) {
                    SpeakerHis history = new SpeakerHis(speaker.getDeviceID(), speaker.getName());
                    reference = database.getReference("SpeakerHis").child(hash).child(speaker.getDeviceID()).child("time").child(date);
                    reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                DataSnapshot snapshot = task.getResult();
                                List<String> time = new ArrayList<>();
                                for (DataSnapshot post : snapshot.getChildren()) {
                                    time.add(post.getValue().toString());
                                }
                                history.time = time;
                                speakerHisList[index].add(history);
                            }
                            Log.wtf("Hoang", "onCompleted ran");
                            latch.countDown();
                        }
                    });
                }

            } else if(typ == 2) {
                servoHisList[index] = new ArrayList<>();
                for (Servo servo : servoList) {
                    ServoHis servoHis = new ServoHis(servo.getDeviceID(), servo.getName());
                    reference = database.getReference("ServoHis").child(hash).child(servo.getDeviceID()).child("time").child(date);
                    reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                DataSnapshot snapshot = task.getResult();
                                for (DataSnapshot post : snapshot.getChildren()) {
                                    String oTime = post.child("oTime").getValue().toString();
                                    String cTime = post.child("cTime").getValue() != null ? post.child("cTime").getValue().toString() : null;
                                    servoHis.setTime(oTime, cTime);
                                    Log.e("oTime", oTime);
                                }
                                servoHisList[index].add(servoHis);
                            }
                            Log.wtf("Hoang", "onCompleted ran");
                            latch.countDown();
                        }
                    });
                }
                Log.e("in", "task");
                Log.wtf("Debug", "getHistory run");
            }
        }

        public void saveResponse(Response response, String catgory) {
            DatabaseReference reference = database.getReference("Response").child(catgory).child(hash);
            reference.setValue(response);
        }

        public void writeUser() {
            ref = database.getReference("User").child(hash);
            ref.child("pin").setValue(user.getPin());
        }

        public byte[] readFile(String file) {
            try {
                InputStream inputStream = context.openFileInput(file);
                int size = inputStream.available();
                byte[] data = new byte[size];
                inputStream.read(data);
                inputStream.close();
                return data;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public void writeFile(byte[] data, String file) {
            try {
                FileOutputStream outputStream = context.openFileOutput(file, Context.MODE_PRIVATE);
                outputStream.write(data);
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    class SendMail extends AsyncTask<Void, Void, String> {

        private Message message;

        public SendMail(Message message) {
            this.message = message;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                Transport.send(this.message);
                return "Success";
            } catch (MessagingException e) {
                e.printStackTrace();
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public ArrayList<String[]> getServoData(List<ServoHis> servoHisList){
        if (servoHisList == null){
            Log.wtf("Hoang", "sensor null roi");
            return null;
        }
        Log.wtf("Hoang", String.valueOf(servoHisList.size()));
        Log.wtf("Debug", "getServoData ran");
        ArrayList<String[]> ret = new ArrayList<>();
        ServoHis tempServo;
        for (int i = 0; i < servoHisList.size(); i ++){
            tempServo = servoHisList.get(i);
            for (int s = 0; s < tempServo.getSize(); s ++) {
                ret.add(new String[]{tempServo.deviceID, tempServo.getOTime(s), tempServo.getCTime(s)});
                Log.wtf("Debug", "Start time: " + tempServo.getOTime(s));
                Log.wtf("Debug", "End time: " + tempServo.getCTime(s));
            }
            Log.wtf("Debug", "Device ID: " + tempServo.deviceID);

        }
        Log.wtf("Debug", "end getServoData");
        return ret;
    }
}
