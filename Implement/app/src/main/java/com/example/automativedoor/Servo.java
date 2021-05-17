package com.example.automativedoor;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.automativedoor.Control.MQTTServer;
import com.example.automativedoor.Control.UserController;
import com.example.automativedoor.GUIControl.ServoAdapter;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;

public class Servo extends AppCompatActivity {

    private ListView listView;

    private ArrayList<com.example.automativedoor.EntityClass.Servo> servos;

    private ServoAdapter adapter;

    private UserController controller = UserController.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servo);
        Log.e("Servo in state: ", "onCreate");

        this.associate();

        adapter = new ServoAdapter(this, R.layout.stream_servo, servos);
        listView.setAdapter(adapter);
    }

    private void associate() {
        listView = (ListView) findViewById(R.id.servo_listview);
        servos = controller.servoList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Servo in state: ", "onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Servo in state: ", "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("Servo in state: ", "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("Servo in state: ", "onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Servo in state: ", "onPause");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("Servo in state: ", "onSaveInstance");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Servo in state: ", "onDestroy");
    }
}