package com.example.automativedoor;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.automativedoor.Control.UserController;
import com.example.automativedoor.EntityClass.Sensor;
import com.example.automativedoor.EntityClass.Servo;
import com.example.automativedoor.EntityClass.Speaker;
import com.example.automativedoor.GUIControl.ComponentAdapter;

import java.util.ArrayList;


public class Component extends AppCompatActivity {
    private ListView listView;

    private ArrayList<Servo> servos;
    private ArrayList<Speaker> speakers;
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

        adapter = new ComponentAdapter(this, R.layout.stream_component, components, servos, speakers);
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

        TextView txt_mode = (TextView) findViewById(R.id.component_mode);
        RelativeLayout layout = findViewById(R.id.component_layout_mode);
        if (controller.getMode() == 2) {
            txt_mode.setText("Anti Thief");
            layout.setBackgroundResource(R.drawable.component_off);
        }
        else if (controller.getMode() == 1) {
            txt_mode.setText("Welcome Guest");
            layout.setBackgroundResource(R.drawable.component_on);
        }
    }

    private void associate() {
        listView = (ListView) findViewById(R.id.component_listview);
        this.servos = controller.servoList;
        this.speakers = controller.speakerList;

        this.components = new ArrayList<com.example.automativedoor.EntityClass.Component>();
        this.components.addAll(this.speakers);
        this.components.addAll(this.servos);
    }

    private void refreshItem() {
        this.associate();
        adapter = new ComponentAdapter(this, R.layout.stream_component, components, servos, speakers);
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
                if (requestCode == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    ViewGroup viewGroup = findViewById(android.R.id.content);
                    View view = LayoutInflater.from(this).inflate(R.layout.sensor_mode, viewGroup, false);
                    builder.setView(view);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    RadioButton anti_thief = (RadioButton) alertDialog.findViewById(R.id.sensor_anti_thief);
                    RadioButton welcome = (RadioButton) alertDialog.findViewById(R.id.sensor_welcome);
                    Button button = (Button) alertDialog.findViewById(R.id.sensor_confirm);
                    ImageView reminder = (ImageView) alertDialog.findViewById(R.id.sensor_reminder);

                    reminder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            startActivity(new Intent(getApplicationContext(), SetTimer.class));
                        }
                    });

                    TextView txt_mode = (TextView) findViewById(R.id.component_mode);
                    RelativeLayout layout = findViewById(R.id.component_layout_mode);
                    if (controller.getMode() == 2) {
                        anti_thief.setChecked(true);
                    }
                    else if (controller.getMode() == 1) {
                        welcome.setChecked(true);
                    }

                    button.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            int mode = 0;
                            if (anti_thief.isChecked()) {
                                mode = 2;
                                txt_mode.setText("Anti Thief");
                                layout.setBackgroundResource(R.drawable.component_off);
                            }
                            else if (welcome.isChecked()) {
                                mode = 1;
                                txt_mode.setText("Welcome Guest");
                                layout.setBackgroundResource(R.drawable.component_on);
                            }
                            controller.changeMode(mode);
                            refreshItem();
                        }
                    });
                }
                else if (requestCode == 2) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    ViewGroup viewGroup = findViewById(android.R.id.content);
                    View view = LayoutInflater.from(this).inflate(R.layout.stream_speaker, viewGroup, false);
                    builder.setView(view);
                    builder.setPositiveButton("Ok", null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    Speaker speaker = controller.speakerList.get(0);
                    TextView textView = alertDialog.findViewById(R.id.speaker_name);
                    textView.setText(speaker.getName());

                    SeekBar seekBar = alertDialog.findViewById(R.id.speaker_seekbar);
                    seekBar.setProgress(speaker.getVolume());

                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        int progressChangedValue = 0;

                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            progressChangedValue = progress;
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            UserController.getInstance().setSpeaker(0, progressChangedValue);
                        }
                    });

                }
                else if (requestCode == 3) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    ViewGroup viewGroup = findViewById(android.R.id.content);
                    View view = LayoutInflater.from(this).inflate(R.layout.stream_servo, viewGroup, false);
                    builder.setView(view);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    Servo servo = controller.servoList.get(0);
                    Button close = alertDialog.findViewById(R.id.servo_close);
                    Button open = alertDialog.findViewById(R.id.servo_open);
                    TextView txt_name = alertDialog.findViewById(R.id.servo_name);

                    txt_name.setText(servo.getName());
                    open.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(View v) {
                            open.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce));
                            Toast notify;
                            if (UserController.getInstance().openDoor()) {
                                notify = Toast.makeText(getApplicationContext(), "Door opened", Toast.LENGTH_SHORT);
                                refreshItem();
                            } else {
                                notify = Toast.makeText(getApplicationContext(), "This door is already opened", Toast.LENGTH_SHORT);
                            }
                            new CountDownTimer(700, 1000) {
                                public void onTick(long millisUntilFinished) {notify.show();}
                                public void onFinish() {notify.cancel();}
                            }.start();

                        }
                    });

                    close.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(View v) {
                            close.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce));
                            Toast notify;
                            if (UserController.getInstance().closeDoor()) {
                                notify = Toast.makeText(getApplicationContext(), "Door closed", Toast.LENGTH_SHORT);
                                refreshItem();
                            } else {
                                notify = Toast.makeText(getApplicationContext(), "This door is already closed", Toast.LENGTH_SHORT);
                            }
                            new CountDownTimer(700, 1000) {
                                public void onTick(long millisUntilFinished) {notify.show();}
                                public void onFinish() {notify.cancel();}
                            }.start();
                        }
                    });
                }
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