package com.example.automativedoor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
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

    private ImageView sensor_btn;
    private ImageView servo_btn;
    private ImageView speaker_btn;
    private TextView sensor_txt;
    private TextView servo_txt;
    private TextView speaker_txt;
    private SwipeRefreshLayout swipe;

    private void sensorClick() { startActivityForResult(new Intent(this, pin.class), 1); }

    private void speakerClick() { startActivityForResult(new Intent(this, pin.class), 2); }

    private void servoClick() { startActivityForResult(new Intent(this, pin.class), 3); }


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
        this.sensor_btn = (ImageView) findViewById(R.id.component_sensor_btn);
        this.sensor_txt = (TextView) findViewById(R.id.component_sensor_text);

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

        this.servo_btn = (ImageView) findViewById(R.id.component_servo_btn);
        this.servo_txt = (TextView) findViewById(R.id.component_servo_text);

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

        this.speaker_btn = (ImageView) findViewById(R.id.component_speaker_btm);
        this.speaker_txt = (TextView) findViewById(R.id.component_speaker_text);

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

        this.swipe = (SwipeRefreshLayout) findViewById(R.id.component_swipe);
        this.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItem();
                swipe.setRefreshing(false);
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

    private void refreshItem() {
        this.associate();
        adapter = new ComponentAdapter(this, R.layout.stream_component, components);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.e("Component in state: ", "onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Log.e("Component in state: ", "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
//        Log.e("Component in state: ", "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        Log.e("Component in state: ", "onRestart");
        this.refreshItem();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Log.e("Component in state: ", "onPause");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        Log.e("Component in state: ", "onSaveInstance");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Log.e("Component in state: ", "onDestroy");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == AppCompatActivity.RESULT_OK) {
            final String result = data.getStringExtra(pin.PIN_RESULT);
            if (result.equals("pin correct")) {
                if (requestCode == 1) startActivity(new Intent(this, com.example.automativedoor.Sensor.class));
                else if (requestCode == 2) startActivity(new Intent(this, com.example.automativedoor.Speaker.class));
                else if (requestCode == 3) startActivity(new Intent(this, com.example.automativedoor.Servo.class));
            }
            else if (result.equals("disable")) {
                String content = "Ai do dang co gang truy cap vao thiet bi cua ban!!! <br> Hay co chinh sach bao ve ma PIN cua ban. <br>";
                content += "<h1>Smart Home App</h1>" +
                        "<img src=\"https://img.docbao.vn/images/uploads/2019/11/11/xa-hoi/smart-home.jpg\" width=\"1000\" height=\"600\">";
                controller.sendMail("Invalid Access", content);
            }
        }
    }

}