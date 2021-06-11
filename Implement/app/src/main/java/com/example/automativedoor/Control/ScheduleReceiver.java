package com.example.automativedoor.Control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;

public class ScheduleReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        int mode = intent.getIntExtra("MODE", 0);
        int del = intent.getIntExtra("DEL", 0);
        int index = intent.getIntExtra("INDEX", 0);

        if (del == 1) {
            try {
                UserController.getInstance().changeMode(mode);
                InputStream inputStream = context.openFileInput("timer.json");
                int size = inputStream.available();
                byte[] data = new byte[size];
                inputStream.read(data);
                inputStream.close();
                String json = new String(data);
                JSONArray array = new JSONArray(json);

                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    if (object.getInt("index") == index) {
                        array.remove(i);
                        break;
                    }
                }

                FileOutputStream outputStream = context.openFileOutput("timer.json", Context.MODE_PRIVATE);
                outputStream.write(array.toString().getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Calendar calendar = Calendar.getInstance();
            String dow = intent.getStringExtra("DOW");
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dow.charAt(dayOfWeek - 1) == '1') {
                UserController.getInstance().changeMode(mode);
            }
        }
    }
}
