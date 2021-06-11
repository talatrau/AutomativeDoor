package com.example.automativedoor;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.automativedoor.Control.ScheduleReceiver;
import com.example.automativedoor.Control.UserController;
import com.example.automativedoor.EntityClass.Sensor;
import com.example.automativedoor.EntityClass.Servo;
import com.example.automativedoor.EntityClass.Speaker;
import com.example.automativedoor.GUIControl.ComponentAdapter;
import com.example.automativedoor.GUIControl.TimerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;


public class Component extends AppCompatActivity {
    private ListView listView;

    private ArrayList<Servo> servos;
    private ArrayList<Speaker> speakers;
    private ArrayList<com.example.automativedoor.EntityClass.Component> components;

    private ComponentAdapter adapter;

    private UserController controller;

    private ImageView sensor_btn;
    private ImageView servo_btn;
    private ImageView speaker_btn;
    private TextView sensor_txt;
    private TextView servo_txt;
    private TextView speaker_txt;
    private SwipeRefreshLayout swipe;

    private SwipeMenuListView schedule;
    private TimerAdapter timerAdapter;
    private JSONArray jsonArray = null;

    private void sensorClick() {
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

    private void speakerClick() {
//        Intent intent = new Intent(this, SpeakerView.class);
//        startActivityForResult(intent, 1);
        controller.loadHistory(2, "2021-06-12", 10);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    controller.latch.await();
                    for (int i = 0; i < controller.servoHisList.length; i++) {
                        Log.e("history " + i, controller.servoHisList[i].get(0).getSize() + "");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 500);
    }

    private void servoClick() {
        Intent intent = new Intent(this, ServoView.class);
        startActivityForResult(intent, 2);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_component);
        Log.e("Component in state: ", "onCreate");

        controller = UserController.getInstance();
        this.associate();
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xFF, 0xFF, 0xFF)));
                deleteItem.setWidth(130);
                deleteItem.setTitle("Delete");
                deleteItem.setIcon(R.drawable.timer_del);
                menu.addMenuItem(deleteItem);
            }
        };
        schedule.setMenuCreator(creator);

        adapter = new ComponentAdapter(this, R.layout.stream_component, components, servos, speakers);
        listView.setAdapter(adapter);

        timerAdapter = new TimerAdapter(this, R.layout.stream_set_timer, this.jsonArray);
        schedule.setAdapter(timerAdapter);

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


        schedule.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                try {
                    JSONObject object = jsonArray.getJSONObject(position);
                    AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(getApplicationContext().ALARM_SERVICE);
                    int idx = object.getInt("index");
                    Intent intent = new Intent(getApplicationContext(), ScheduleReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), idx, intent, 0);
                    alarmManager.cancel(pendingIntent);
                    jsonArray.remove(position);
                    controller.writeJson(jsonArray, "timer.json");
                    refreshItem();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return true;
            }
        });

        schedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONArray array = jsonArray;
                    JSONObject object = array.getJSONObject(position);

                    String dow = object.getString("dow");
                    boolean[] dayofweek = {false, false, false, false, false, false, false};
                    for (int i = 0; i < 7; i++) {
                        if (dow.charAt(i) == '1') dayofweek[i] = true;
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Repeat every");
                    String[] items = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
                    builder.setMultiChoiceItems(items, dayofweek, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        }
                    });
                    builder.setPositiveButton("Confirm", null);

                    AlertDialog alertDialog = builder.create();
                    alertDialog.getListView().setEnabled(false);
                    alertDialog.show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void associate() {
        this.listView = (ListView) findViewById(R.id.component_listview);
        this.schedule = (SwipeMenuListView) findViewById(R.id.component_timer_listview);
        this.servos = controller.servoList;
        this.speakers = controller.speakerList;

        this.components = new ArrayList<>();
        this.components.addAll(this.speakers);
        this.components.addAll(this.servos);

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

        this.jsonArray = controller.readJson("timer.json");
    }

    public void refreshItem() {
        this.associate();
        adapter = new ComponentAdapter(this, R.layout.stream_component, components, servos, speakers);
        listView.setAdapter(adapter);

        timerAdapter = new TimerAdapter(this, R.layout.stream_set_timer, this.jsonArray);
        schedule.setAdapter(timerAdapter);

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
        this.refreshItem();
//        Log.e("Component in state: ", "onRestart");
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
            refreshItem();
        }
    }

}