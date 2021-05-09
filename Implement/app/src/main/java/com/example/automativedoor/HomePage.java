package com.example.automativedoor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD
        setContentView(R.layout.temp_activity);

        Log.e("App in state: ", "onCreate");
=======
        setContentView(R.layout.activity_home_page);
        Log.e("Homepage in state: ", "onCreate");
>>>>>>> 3880ecd077dcf98ca2836fb2faa7d4aef162a043
    }

    @Override
    protected void onResume() {
        super.onResume();
<<<<<<< HEAD
        Log.e("App in state: ", "onResume");
=======
        Log.e("Homepage in state: ", "onResume");
>>>>>>> 3880ecd077dcf98ca2836fb2faa7d4aef162a043
    }

    @Override
    protected void onStart() {
        super.onStart();
<<<<<<< HEAD
        Log.e("App in state: ", "onStart");
=======
        Log.e("Homepage in state: ", "onStart");
>>>>>>> 3880ecd077dcf98ca2836fb2faa7d4aef162a043
    }

    @Override
    protected void onStop() {
        super.onStop();
<<<<<<< HEAD
        Log.e("App in state: ", "onStop");
=======
        Log.e("Homepage in state: ", "onStop");
>>>>>>> 3880ecd077dcf98ca2836fb2faa7d4aef162a043
    }

    @Override
    protected void onRestart() {
        super.onRestart();
<<<<<<< HEAD
        Log.e("App in state: ", "onRestart");
=======
        Log.e("Homepage in state: ", "onRestart");
>>>>>>> 3880ecd077dcf98ca2836fb2faa7d4aef162a043
    }

    @Override
    protected void onPause() {
        super.onPause();
<<<<<<< HEAD
        Log.e("App in state: ", "onPause");
=======
        Log.e("Homepage in state: ", "onPause");
>>>>>>> 3880ecd077dcf98ca2836fb2faa7d4aef162a043
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
<<<<<<< HEAD
        Log.e("App in state: ", "onSaveInstance");
=======
        Log.e("Homepage in state: ", "onSaveInstance");
>>>>>>> 3880ecd077dcf98ca2836fb2faa7d4aef162a043
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
<<<<<<< HEAD
        Log.e("App in state: ", "onDestroy");
=======
        Log.e("Homepage in state: ", "onDestroy");
>>>>>>> 3880ecd077dcf98ca2836fb2faa7d4aef162a043
    }
}