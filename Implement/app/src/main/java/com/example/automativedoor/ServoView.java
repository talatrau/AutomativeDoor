package com.example.automativedoor;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.automativedoor.Control.UserController;
import com.example.automativedoor.EntityClass.Servo;

public class ServoView extends AppCompatActivity {

    TextView textView;
    Button open, close;

    private Servo servo;
    private UserController controller = UserController.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.scale_y_out, 0);
        setContentView(R.layout.stream_servo);

        servo = controller.servoList.get(0);
        textView = findViewById(R.id.servo_name);
        open = findViewById(R.id.servo_open);
        close = findViewById(R.id.servo_close);

        setResult(AppCompatActivity.RESULT_OK);
        this.setUpEvent();
    }

    private void setUpEvent() {
        textView.setText(servo.getName());

        open.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                open.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce));
                Toast notify;
                if (controller.openDoor()) {
                    notify = Toast.makeText(getApplicationContext(), "Door opened", Toast.LENGTH_SHORT);
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
                if (controller.closeDoor()) {
                    notify = Toast.makeText(getApplicationContext(), "Door closed", Toast.LENGTH_SHORT);
                } else {
                    notify = Toast.makeText(getApplicationContext(), "This door is already closed", Toast.LENGTH_SHORT);
                }
                new CountDownTimer(700, 1000) {
                    public void onTick(long millisUntilFinished) {notify.show();}
                    public void onFinish() {notify.cancel();}
                }.start();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ServoView.this);
                final EditText input = new EditText(getApplicationContext());
                input.setHint("   Change speaker name");
                input.setTextColor(Color.parseColor("#FFFFFFFF"));
                builder.setView(input);
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text = input.getText().toString();
                        if (!text.isEmpty()) {
                            servo.updateName(text);
                            textView.setText(text);
                        }
                    }
                });
                builder.create().show();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.scale_y_in);
    }
}
