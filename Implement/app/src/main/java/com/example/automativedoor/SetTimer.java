package com.example.automativedoor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.automativedoor.Control.ScheduleReceiver;
import com.example.automativedoor.Control.UserController;
import com.example.automativedoor.GUIControl.TimerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.Arrays;
import java.util.Calendar;


public class SetTimer extends AppCompatActivity {

    TimePicker timePicker;
    TextView txt_repeat, txt_repeat_val, txt_label, txt_label_val;
    TextView txt_save, txt_cancel;
    Switch aSwitch;

    private boolean[] dayOfWeek = {false, false, false, false, false, false, false};
    private boolean action = false;
    private int timerMin;
    private int timerHour;
    private JSONArray jsonArray = null;
    final private UserController controller = UserController.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_set_timer);

        timePicker = (TimePicker) findViewById(R.id.setTimer_timer);
        txt_repeat = (TextView) findViewById(R.id.setTimer_txt_repeat);
        txt_repeat_val = (TextView) findViewById(R.id.setTimer_txt_repeat_val);
        txt_label = (TextView) findViewById(R.id.setTimer_txt_label);
        txt_label_val = (TextView) findViewById(R.id.setTimer_txt_label_val);
        txt_save = (TextView) findViewById(R.id.settimer_save);
        txt_cancel = (TextView) findViewById(R.id.setTimer_cancel);
        aSwitch = (Switch) findViewById(R.id.setTimer_action_val);

        timePicker.setIs24HourView(true);
        this.timerMin = timePicker.getMinute();
        this.timerHour = timePicker.getHour();

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

        this.jsonArray = controller.readJson("timer.json");

        this.setEvent();
    }

    private void setEvent() {
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                timerHour = hourOfDay;
                timerMin = minute;
            }
        });

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject object = getCurrentTimer();
                    int index = (int) (System.currentTimeMillis() % 1000000000);
                    object.put("index", index);
                    jsonArray.put(object);
                    controller.writeJson(jsonArray, "timer.json");

                    int hour = object.getInt("hour");
                    int min = object.getInt("minute");
                    int mode = object.getInt("mode");
                    String dow = object.getString("dow");

                    schedule(dow, mode, hour, min, index);
                    Toast.makeText(SetTimer.this, "Save Success!", Toast.LENGTH_LONG).show();
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                action = isChecked;
                if (isChecked) {
                    TextView textView = findViewById(R.id.setTimer_mode);
                    textView.setText("Anti Thief");
                    textView.setTextColor(Color.parseColor("#FFD50000"));
                    aSwitch.setBackgroundResource(R.color.red);
                }
                else {
                    TextView textView = findViewById(R.id.setTimer_mode);
                    textView.setText("Welcome Guest");
                    textView.setTextColor(Color.parseColor("#FF00E676"));
                    aSwitch.setBackgroundResource(R.color.green);
                }
            }
        });

    }


    private JSONObject getCurrentTimer() throws JSONException {
        String dow = "";
        for (int i = 0; i < 7; i++) {
            dow += dayOfWeek[i] ? "1" : "0";
            dayOfWeek[i] = false;
        }
        int act = action ? 2 : 1;
        aSwitch.setChecked(false);
        String label = txt_label_val.getText().toString();
        label = label.substring(0, label.length() - 2);
        txt_label_val.setText("None >");
        txt_repeat_val.setText("no repeat >");

        JSONObject timer = new JSONObject();
        timer.put("hour", timerHour);
        timer.put("minute", timerMin);
        timer.put("dow", dow);
        timer.put("mode", act);
        timer.put("label", label);

        return timer;
    }

    public void schedule(String dow, int mode, int hour, int min, int index) {
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long timeInMillis = calendar.getTimeInMillis();
        if (timeInMillis < System.currentTimeMillis()) {
            timeInMillis += AlarmManager.INTERVAL_DAY;
        }

        if (dow.equals("0000000")) {
            Intent intent = new Intent(getApplicationContext(), ScheduleReceiver.class);
            intent.putExtra("MODE", mode);
            intent.putExtra("DEL", 1);
            intent.putExtra("INDEX", index);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), index, intent, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        }
        else {
            Intent intent = new Intent(getApplicationContext(), ScheduleReceiver.class);
            intent.putExtra("MODE", mode);
            intent.putExtra("DOW", dow);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), index, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, 24 * 60 * 60 * 1000, pendingIntent);
        }
    }

    public void repeatClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select day of week");
        boolean[] seleted = dayOfWeek.clone();
        String[] items = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        builder.setMultiChoiceItems(items, seleted, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                seleted[which] = isChecked;
            }
        });
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean[] nocheck = {false, false, false, false, false, false, false};
                boolean[] checkall = {true, true, true, true, true, true, true};
                dayOfWeek = seleted;

                if (Arrays.equals(seleted, nocheck)) {
                    txt_repeat_val.setText("no repeat >");
                }
                else if (Arrays.equals(seleted, checkall)) {
                    txt_repeat_val.setText("every day >");
                }
                else {
                    String str = "";
                    if (dayOfWeek[0]) str += ",Sun";
                    if (dayOfWeek[1]) str += ",Mon";
                    if (dayOfWeek[2]) str += ",Tue";
                    if (dayOfWeek[3]) str += ",Wed";
                    if (dayOfWeek[4]) str += ",Thu";
                    if (dayOfWeek[5]) str += ",Fri";
                    if (dayOfWeek[6]) str += ",Sat";
                    txt_repeat_val.setText(str.substring(1) + " >");
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void labelClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        builder.setView(input);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                txt_label_val.setText(input.getText().toString() + " >");
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }
}