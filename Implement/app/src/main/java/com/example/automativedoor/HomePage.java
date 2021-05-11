package com.example.automativedoor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.automativedoor.Control.UserController;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomePage extends AppCompatActivity {

    private boolean doubleBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_page);
        Log.e("Homepage in state: ", "onCreate");

        ImageButton button = (ImageButton) findViewById(R.id.component_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.startAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.bounce));
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("Account");
                reference.setValue("cc ne ha");
                Toast.makeText(HomePage.this, "ok", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Homepage in state: ", "onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Homepage in state: ", "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("Homepage in state: ", "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("Homepage in state: ", "onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Homepage in state: ", "onPause");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("Homepage in state: ", "onSaveInstance");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Homepage in state: ", "onDestroy");
    }

    @Override
    public void onBackPressed() {
        if (this.doubleBack) {
            UserController.logout();
            startActivity(new Intent(getApplicationContext(), login.class));
        }

        this.doubleBack = true;
        Toast.makeText(this, "Please click BACK again to logout", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBack = false;
            }
        }, 2000);
    }
}