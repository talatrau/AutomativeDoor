package com.example.automativedoor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.automativedoor.Control.UserController;

public class HomePage extends AppCompatActivity {

    private boolean doubleBack = false;

    private UserController controller = UserController.getInstance();


    private void componentClick() {
        startActivity(new Intent(this, Component.class));
    }

    private void historyClick() {
        startActivity(new Intent(this, History.class));
    }

    private void responseClick() {
        startActivity(new Intent(this, feedback.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_page);
        Log.e("Homepage in state: ", "onCreate");

        this.setUpButtonEvent();
        UserController.getInstance().context = this;
        UserController.getInstance().setMqttServer();
    }

    private void setUpButtonEvent() {
        ImageButton component_bnt = (ImageButton) findViewById(R.id.component_button);
        TextView component_txt = (TextView) findViewById(R.id.component_button_text);

        component_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                component_bnt.startAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.bounce));
                component_txt.startAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.bounce));
                componentClick();
            }
        });

        component_bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                component_bnt.startAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.bounce));
                component_txt.startAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.bounce));
                componentClick();
            }
        });

        ImageButton history_bnt = (ImageButton) findViewById(R.id.history_button);
        TextView history_txt = (TextView) findViewById(R.id.history_button_text);

        history_bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                history_bnt.startAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.bounce));
                history_txt.startAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.bounce));
                historyClick();
            }
        });

        history_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                history_bnt.startAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.bounce));
                history_txt.startAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.bounce));
                historyClick();
            }
        });

        ImageButton response_bnt = (ImageButton) findViewById(R.id.response_button);
        TextView response_txt = (TextView) findViewById(R.id.response_button_text);

        response_bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                response_bnt.startAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.bounce));
                response_txt.startAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.bounce));
                responseClick();
            }
        });

        response_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                response_bnt.startAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.bounce));
                response_txt.startAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.bounce));
                responseClick();
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
            controller.fauth.signOut();
            startActivity(new Intent(getApplicationContext(), login.class));
            finish();
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