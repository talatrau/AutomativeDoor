package com.example.automativedoor.Control;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;

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
import com.google.android.gms.tasks.OnSuccessListener;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.util.Properties;
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
    private int serviceCount = 0;


    public List<SensorHis> sensorHisList;
    public List<ServoHis> servoHisList;
    public List<SpeakerHis> speakerHisList;

    final public FirebaseAuth fauth = FirebaseAuth.getInstance();

    public Context context;
    public Account user;
    public ArrayList<Sensor> sensorList;
    public ArrayList<Speaker> speakerList;
    public ArrayList<Servo> servoList;

    private UserController() {
        driver = new DatabaseDriver();
    }

    public static UserController getInstance() {
        if (instance == null) {
            instance =  new UserController();
            return instance;
        }
        return instance;
    }

    public int getSpeakerIndex(String deviceID) {
        for (int i = 0; i < speakerList.size(); i++) {
            if (speakerList.get(i).getDeviceID().equals(deviceID)) return i;
        }
        return -1;
    }

    public int getServoIndex(String deviceID) {
        for (int i = 0; i < servoList.size(); i++) {
            if (servoList.get(i).getDeviceID().equals(deviceID)) return i;
        }
        return -1;
    }

    public void setMqttSubcribe(String feedKey, boolean signal) {
        if (signal) {
            if (this.serviceCount == 0) {
                context.startService(new Intent(context, SensorService.class));
            }
            this.serviceCount += 1;
            this.mqttServer_2.subscribeToTopic(feedKey);
        }
        else {
            if (this.serviceCount == 1) {
                context.stopService(new Intent(context, SensorService.class));
            }
            this.serviceCount -= 1;
            this.mqttServer_2.unsubscribeToTopic(feedKey);
        }
    }

    public void closeMqttConnection() {
        try {
            if (serviceCount == 0) {
                this.mqttServer_1.mqttAndroidClient.disconnect();
                this.mqttServer_1 = null;

                this.mqttServer_2.mqttAndroidClient.disconnect();
                this.mqttServer_2 = null;

                Log.e("mqtt connection", "disconnected");
            }
        } catch (MqttException e) {
            Log.e("Error when close mqtt", e.toString());
        }
    }

    public void setMqttServer() {
        if (this.mqttServer_2 == null) {
            this.mqttServer_2 = new MQTTServer(context, "MqttService1", "1", "KEY");
            this.trackingSensor();
        }
        if (this.mqttServer_1 == null) {
            this.mqttServer_1 = new MQTTServer(context, "MqttService", "0", "KEY");
        }
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
    public void alarmSpeaker(boolean signal, int position) {
        String mess = this.speakerList.get(position).alarm(signal);
        String topic = this.speakerList.get(position).getMqttTopic();
        this.mqttServer_1.publishMqtt(mess, topic);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void turnOnSensor(int position) {
        if (this.sensorList.get(position).toggle(true)) {
            //this.sendDataMQTT("ON", "CongTuVu/feeds/automativedoor.sensor\n");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void turnOffSensor(int position) {
        if (this.sensorList.get(position).toggle(false)) {
            //this.sendDataMQTT("OFF", "CongTuVu/feeds/automativedoor.sensor\n");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean openDoor(int position) {
        Log.e("open the door", "message");
        if (this.servoList.get(position).toggle(true)) {
            String topic = this.servoList.get(position).getMqttTopic();
            this.mqttServer_2.publishMqtt("{\n" +
                    "\"id\":\"17\",\n" +
                    "\"name\":\"SERVO\",\n" +
                    "\"data\":\"180\",\n" +
                    "\"unit\":\"degree\"\n" +
                    "}\n", topic);
            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean closeDoor(int position) {
        Log.e("close the door", "message");
        if (this.servoList.get(position).toggle(false)) {
            String topic = this.servoList.get(position).getMqttTopic();
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

    public void trackingSensor() {
        mqttServer_2.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                Log.w("mqtt", serverURI);
            }

            @Override
            public void connectionLost(Throwable cause) {
                Log.w("Lost mqtt", cause.toString());
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                JSONObject jsonObject = new JSONObject(message.toString());
                int index = 0;
                for (int i = 0; i < sensorList.size(); i++) {
                    if (sensorList.get(i).getMqttTopic().equals(topic)) index = i;
                }
                String data = jsonObject.getString("data");
                Log.w("message arrived", message.toString());
                sensorList.get(index).processConnect(data);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

    }

    public void sendResponse(int score, String text) {
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
                        Response response = new Response(score, text);
                        driver.saveResponse(response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("cannot open", "spam.txt");
            }
        }
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

    public DatabaseReference setup() {
        this.hash = md5Hash(fauth.getCurrentUser().getEmail());
        driver.readComponent();
        return  driver.readUser();
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
                    String message = "Your password is <u>" + user.getPass() + "</u>. <br>" ;
                    message += "<h1>Smart Home App</h1>" +
                            "<img src=\"https://img.docbao.vn/images/uploads/2019/11/11/xa-hoi/smart-home.jpg\" width=\"1000\" height=\"600\">";
                    sendMail("Forgot Pass", message);
                    hash = null;
                    user = null;
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
            DatabaseReference reference = database.getReference("Response").child(hash);
            reference.setValue(response);
        }

        public void writeUser() {
            ref = database.getReference("User").child(hash);
            ref.child("pin").setValue(user.getPin());
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

}
