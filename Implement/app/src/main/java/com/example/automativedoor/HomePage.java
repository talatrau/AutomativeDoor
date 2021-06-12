package com.example.automativedoor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.automativedoor.Control.UserController;

public class HomePage extends AppCompatActivity {

    TextView component_txt, history_txt, response_txt;
    ImageButton component_bnt, history_bnt, response_bnt;

    private boolean doubleBack = false;

    private UserController controller = UserController.getInstance();

    private void componentClick() {
        Intent intent = new Intent(this, pin.class);
        startActivityForResult(intent, 1);
    }

    private void historyClick() {
        startActivity(new Intent(this, History.class));
    }

    private void responseClick() {
        Intent intent = new Intent(this, Feedback.class);
        startActivityForResult(intent, 0);
    }

    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadeout, R.anim.fadein);

        setContentView(R.layout.activity_home_page);
        Log.e("Homepage in state: ", "onCreate");

        this.setUpButtonEvent();
        UserController.getInstance().context = this;
        UserController.getInstance().setMqttServer();
    }

    private void setUpButtonEvent() {
        component_bnt = (ImageButton) findViewById(R.id.component_button);
        component_txt = (TextView) findViewById(R.id.component_button_text);

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

        history_bnt = (ImageButton) findViewById(R.id.history_button);
        history_txt = (TextView) findViewById(R.id.history_button_text);

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

        response_bnt = (ImageButton) findViewById(R.id.response_button);
        response_txt = (TextView) findViewById(R.id.response_button_text);

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
        UserController.getInstance().closeMqttConnection();
        Log.e("Homepage in state: ", "onDestroy");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0)  {
            if (resultCode == AppCompatActivity.RESULT_CANCELED) {
                Toast.makeText(this, "Please try again after 60 seconds", Toast.LENGTH_SHORT).show();
                response_bnt.setEnabled(false);
                response_txt.setEnabled(false);
                countDownTimer = new CountDownTimer(30000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        response_bnt.setEnabled(true);
                        response_txt.setEnabled(true);
                    }
                };
                countDownTimer.start();
            }
        }
        else if (requestCode == 1) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                final String result = data.getStringExtra(pin.PIN_RESULT);
                if (result.equals("pin correct")) startActivity(new Intent(this, Component.class));
                else {
                    String content = "Ai do dang co gang truy cap vao thiet bi cua ban!!! <br> Hay co chinh sach bao ve ma PIN va tai khoan cua ban. <br>";
                    content += "<h1>Smart Home App</h1>" +
                            "<img src=\"https://img.docbao.vn/images/uploads/2019/11/11/xa-hoi/smart-home.jpg\" width=\"1000\" height=\"600\">";
                    controller.sendMail("Invalid Access", content);
                    component_bnt.setEnabled(false);
                    component_txt.setEnabled(false);
                    countDownTimer = new CountDownTimer(30000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            component_bnt.setEnabled(true);
                            component_txt.setEnabled(true);
                        }
                    };
                    countDownTimer.start();
                }
            }
        }
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