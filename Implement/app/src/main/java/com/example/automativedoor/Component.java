package com.example.automativedoor;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.automativedoor.GUIControl.ComponentAdapter;

import java.util.ArrayList;

public class Component extends AppCompatActivity {
    ListView listView;
    ArrayList<com.example.automativedoor.EntityClass.Component> components;
    ComponentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_component);
        Log.e("Component in state: ", "onCreate");

        this.associate();

        adapter = new ComponentAdapter(this, R.layout.stream_component, components);
        listView.setAdapter(adapter);
    }

    private void associate() {
        listView = (ListView) findViewById(R.id.component_listview);
        components = new ArrayList<>();

        components.add(new com.example.automativedoor.EntityClass.Servo("001", "cua truoc", true));
        components.add(new com.example.automativedoor.EntityClass.Servo("001", "cua sau", true));
        components.add(new com.example.automativedoor.EntityClass.Sensor("001", "cua truoc", false));

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