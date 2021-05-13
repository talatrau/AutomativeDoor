package com.example.automativedoor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.automativedoor.Control.UserController;
import com.example.automativedoor.EntityClass.Sensor;
import com.example.automativedoor.EntityClass.Servo;
import com.example.automativedoor.EntityClass.Speaker;
import com.example.automativedoor.GUIControl.ComponentAdapter;

import java.util.ArrayList;

public class Component extends AppCompatActivity {
    private ListView listView;

    private ArrayList<com.example.automativedoor.EntityClass.Component> components;

    private ComponentAdapter adapter;

    private UserController controller = UserController.getInstance();


    private void sensorClick() { startActivity(new Intent(this, com.example.automativedoor.Sensor.class)); }

    private void speakerClick() { startActivity(new Intent(this, com.example.automativedoor.Speaker.class)); }

    private void servoClick() { startActivity(new Intent(this, com.example.automativedoor.Servo.class)); }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_component);
        Log.e("Component in state: ", "onCreate");

        this.associate();

        adapter = new ComponentAdapter(this, R.layout.stream_component, components);
        listView.setAdapter(adapter);

        this.setUpButtonEvent();
    }

    private void setUpButtonEvent() {
        ImageView sensor_btn = (ImageView) findViewById(R.id.component_sensor_btn);
        TextView sensor_txt = (TextView) findViewById(R.id.component_sensor_text);

        sensor_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensor_btn.startAnimation(AnimationUtils.loadAnimation(Component.this, R.anim.bounce));
                sensor_txt.startAnimation(AnimationUtils.loadAnimation(Component.this, R.anim.bounce));
                sensorClick();
            }
        });

        sensor_txt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sensor_btn.startAnimation(AnimationUtils.loadAnimation(Component.this, R.anim.bounce));
                sensor_txt.startAnimation(AnimationUtils.loadAnimation(Component.this, R.anim.bounce));
                sensorClick();
            }
        });

        ImageView servo_btn = (ImageView) findViewById(R.id.component_servo_btn);
        TextView servo_txt = (TextView) findViewById(R.id.component_servo_text);

        servo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                servo_btn.startAnimation(AnimationUtils.loadAnimation(Component.this, R.anim.bounce));
                servo_txt.startAnimation(AnimationUtils.loadAnimation(Component.this, R.anim.bounce));
                servoClick();
            }
        });

        servo_txt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                servo_btn.startAnimation(AnimationUtils.loadAnimation(Component.this, R.anim.bounce));
                servo_txt.startAnimation(AnimationUtils.loadAnimation(Component.this, R.anim.bounce));
                servoClick();
            }
        });

        ImageView speaker_btn = (ImageView) findViewById(R.id.component_speaker_btm);
        TextView speaker_txt = (TextView) findViewById(R.id.component_speaker_text);

        speaker_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speaker_btn.startAnimation(AnimationUtils.loadAnimation(Component.this, R.anim.bounce));
                speaker_txt.startAnimation(AnimationUtils.loadAnimation(Component.this, R.anim.bounce));
                speakerClick();
            }
        });

        speaker_txt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                speaker_btn.startAnimation(AnimationUtils.loadAnimation(Component.this, R.anim.bounce));
                speaker_txt.startAnimation(AnimationUtils.loadAnimation(Component.this, R.anim.bounce));
                speakerClick();
            }
        });
    }

    private void associate() {
        listView = (ListView) findViewById(R.id.component_listview);
        components = new ArrayList<com.example.automativedoor.EntityClass.Component>();

        for (Sensor sensor : controller.sensorList) {
            components.add(sensor);
        }

        for (Speaker speaker : controller.speakerList) {
            components.add(speaker);
        }

        for (Servo servo : controller.servoList) {
            components.add(servo);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Component in state: ", "onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Component in state: ", "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("Component in state: ", "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("Component in state: ", "onRestart");
        this.associate();
        adapter = new ComponentAdapter(this, R.layout.stream_component, components);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Component in state: ", "onPause");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("Component in state: ", "onSaveInstance");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Component in state: ", "onDestroy");
    }
}