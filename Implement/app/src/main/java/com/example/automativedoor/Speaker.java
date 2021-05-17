package com.example.automativedoor;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.automativedoor.Control.MQTTServer;
import com.example.automativedoor.Control.UserController;
import com.example.automativedoor.GUIControl.SpeakerAdapter;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;

public class Speaker extends AppCompatActivity {

    private ListView listView;

    private ArrayList<com.example.automativedoor.EntityClass.Speaker> speakers;

    private SpeakerAdapter adapter;

    private UserController controller = UserController.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker);
        Log.e("Speaker in state: ", "onCreate");

        this.associate();

        adapter = new SpeakerAdapter(this, R.layout.stream_speaker, speakers);
        listView.setAdapter(adapter);
    }

    private void associate() {
        listView = (ListView) findViewById(R.id.speaker_listview);
        speakers = controller.speakerList;
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Speaker in state: ", "onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Speaker in state: ", "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("Speaker in state: ", "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("Speaker in state: ", "onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Speaker in state: ", "onPause");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("Speaker in state: ", "onSaveInstance");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Speaker in state: ", "onDestroy");
    }
}