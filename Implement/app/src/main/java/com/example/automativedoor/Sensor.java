package com.example.automativedoor;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.automativedoor.Control.UserController;
import com.example.automativedoor.GUIControl.SensorAdapter;

import java.util.ArrayList;

public class Sensor extends AppCompatActivity {

    private ListView listView;

    private ArrayList<com.example.automativedoor.EntityClass.Sensor> sensors;

    private SensorAdapter adapter;

    private UserController controller = UserController.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        Log.e("Sensor in state: ", "onCreate");

        this.associate();

        adapter = new SensorAdapter(this, R.layout.stream_sensor, sensors);
        listView.setAdapter(adapter);
    }

    private void associate() {
        listView = (ListView) findViewById(R.id.sensor_listview);
        sensors = controller.sensorList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Sensor in state: ", "onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Sensor in state: ", "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("Sensor in state: ", "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("Sensor in state: ", "onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Sensor in state: ", "onPause");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("Sensor in state: ", "onSaveInstance");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Sensor in state: ", "onDestroy");
    }
}