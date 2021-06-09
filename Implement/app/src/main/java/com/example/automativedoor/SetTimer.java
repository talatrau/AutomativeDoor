package com.example.automativedoor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
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


public class SetTimer extends AppCompatActivity {

    TimePicker timePicker;
    TextView txt_repeat, txt_repeat_val, txt_label, txt_label_val;
    TextView txt_save, txt_cancel;
    Switch aSwitch;

    private int index;
    private boolean[] dayOfWeek = {false, false, false, false, false, false, false};
    private boolean action = false;
    private int timerMin;
    private int timerHour;
    private JSONArray jsonArray = null;
    private JSONObject jsonTimer = null;
    private TimerAdapter adapter;
    private SwipeMenuListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_timer);

        timePicker = (TimePicker) findViewById(R.id.setTimer_timer);
        txt_repeat = (TextView) findViewById(R.id.setTimer_txt_repeat);
        txt_repeat_val = (TextView) findViewById(R.id.setTimer_txt_repeat_val);
        txt_label = (TextView) findViewById(R.id.setTimer_txt_label);
        txt_label_val = (TextView) findViewById(R.id.setTimer_txt_label_val);
        txt_save = (TextView) findViewById(R.id.settimer_save);
        txt_cancel = (TextView) findViewById(R.id.setTimer_cancel);
        aSwitch = (Switch) findViewById(R.id.setTimer_action_val);
        listView = (SwipeMenuListView) findViewById(R.id.setTimer_listview);

        timePicker.setIs24HourView(true);
        this.timerMin = timePicker.getMinute();
        this.timerHour = timePicker.getHour();

        this.index = 0;

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

        listView.setMenuCreator(creator);

        this.setEvent();
        this.parseJson();
        this.associate();
    }

    private void associate() {
        JSONArray adapterElement;
        try {
            adapterElement = jsonTimer.getJSONArray("timer");
        } catch (Exception e) {
            adapterElement = new JSONArray();
        }
        adapter = new TimerAdapter(this, R.layout.stream_set_timer, adapterElement);
        listView.setAdapter(adapter);
    }

    private void parseJson() {
        String json = null;
        try {
            InputStream inputStream = openFileInput("timer.json");
            int size = inputStream.available();
            byte[] data = new byte[size];
            inputStream.read(data);
            inputStream.close();
            json = new String(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("json", json);
        try {
            this.jsonArray = new JSONArray(json);
            int len = this.jsonArray.length();
            for (int i = 0; i < len; i++) {
                JSONObject jsonObject = this.jsonArray.getJSONObject(i);
                int index = (int) jsonObject.get("index");
                if (index == this.index) {
                    this.jsonTimer = jsonObject;
                    this.jsonArray.remove(i);
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            this.jsonArray = new JSONArray();
        }
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
                    FileOutputStream outputStream = openFileOutput("timer.json", MODE_PRIVATE);
                    JSONArray timerArray;
                    if (jsonTimer == null) {
                        jsonTimer = new JSONObject();
                        jsonTimer.put("index", index);
                        timerArray = new JSONArray();
                    }
                    else {
                        timerArray = jsonTimer.getJSONArray("timer");
                    }
                    timerArray.put(getCurrentTimer());
                    jsonTimer.put("timer", timerArray);
                    jsonArray.put(jsonTimer);
                    outputStream.write(jsonArray.toString().getBytes());
                    outputStream.close();

                    associate();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                action = isChecked;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONArray array = jsonTimer.getJSONArray("timer");
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

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                try {
                    JSONArray array = jsonTimer.getJSONArray("timer");
                    array.remove(position);
                    jsonTimer.put("timer", array);
                    FileOutputStream outputStream = openFileOutput("timer.json", MODE_PRIVATE);
                    jsonArray.put(jsonTimer);
                    outputStream.write(jsonArray.toString().getBytes());
                    outputStream.close();
                    associate();
                } catch (JSONException | FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return true;
            }
        });

    }


    private JSONObject getCurrentTimer() throws JSONException {
        String dow = "";
        for (int i = 0; i < 7; i++) {
            dow += dayOfWeek[i] ? "1" : "0";
            dayOfWeek[i] = false;
        }
        String act = action ? "ON" : "OFF";
        aSwitch.setChecked(false);
        String label = txt_label_val.getText().toString();
        label = label.substring(0, label.length() - 2);
        txt_label_val.setText("None");

        JSONObject timer = new JSONObject();
        timer.put("hour", timerHour);
        timer.put("minute", timerMin);
        timer.put("dow", dow);
        timer.put("action", act);
        timer.put("label", label);
        return timer;
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
                dayOfWeek = seleted;
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