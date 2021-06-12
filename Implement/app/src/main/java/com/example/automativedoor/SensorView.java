package com.example.automativedoor;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.automativedoor.Control.UserController;

public class SensorView extends AppCompatActivity {

    RadioButton anti_thief, welcome;
    ImageView reminder;
    Button button;

    private UserController controller = UserController.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.scale_y_out, 0);
        setContentView(R.layout.sensor_mode);

        anti_thief = (RadioButton) findViewById(R.id.sensor_anti_thief);
        welcome = (RadioButton) findViewById(R.id.sensor_welcome);
        button = (Button) findViewById(R.id.sensor_confirm);
        reminder = (ImageView) findViewById(R.id.sensor_reminder);

        this.setUpEvent();
        this.setResult(AppCompatActivity.RESULT_OK);
    }

    private void setUpEvent() {
        reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SensorView.this, SetTimer.class);
                startActivity(intent);
                finish();
            }
        });

        if (controller.getMode() == 1) welcome.setChecked(true);
        else if (controller.getMode() == 2) anti_thief.setChecked(true);

        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                int mode = 0;
                if (anti_thief.isChecked()) {
                    mode = 2;
                }
                else if (welcome.isChecked()) {
                    mode = 1;
                }
                controller.changeMode(mode);
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.scale_y_in);
    }
}
