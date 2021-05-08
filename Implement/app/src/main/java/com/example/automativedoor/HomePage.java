package com.example.automativedoor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_activity);

        Log.e("App in state: ", "onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("App in state: ", "onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("App in state: ", "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("App in state: ", "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("App in state: ", "onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("App in state: ", "onPause");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("App in state: ", "onSaveInstance");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("App in state: ", "onDestroy");
    }
}