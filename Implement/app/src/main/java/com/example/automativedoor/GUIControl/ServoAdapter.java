package com.example.automativedoor.GUIControl;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.automativedoor.EntityClass.Servo;
import com.example.automativedoor.R;


import java.util.List;

public class ServoAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Servo> servos;

    public ServoAdapter(Context context, int layout, List<Servo> servos) {
        this.context = context;
        this.layout = layout;
        this.servos = servos;
    }

    @Override
    public int getCount() {
        return servos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout, null);

        TextView txtName = convertView.findViewById(R.id.servo_name);
        Button open = convertView.findViewById(R.id.servo_open);
        Button close = convertView.findViewById(R.id.servo_close);

        Servo servo = servos.get(position);
        txtName.setText(servo.name);

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open.startAnimation(AnimationUtils.loadAnimation(context, R.anim.bounce));
                Toast notify;
                if (!servo.GetState()) {
                    notify = Toast.makeText(context.getApplicationContext(), "Door opened", Toast.LENGTH_SHORT);
                    servo.Toggle(true);
                } else {
                    notify = Toast.makeText(context.getApplicationContext(), "This door is already opened", Toast.LENGTH_SHORT);
                }
                new CountDownTimer(700, 1000) {
                    public void onTick(long millisUntilFinished) {notify.show();}
                    public void onFinish() {notify.cancel();}
                }.start();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close.startAnimation(AnimationUtils.loadAnimation(context, R.anim.bounce));
                Toast notify;
                if (servo.GetState()) {
                    notify = Toast.makeText(context.getApplicationContext(), "Door closed", Toast.LENGTH_SHORT);
                    servo.Toggle(false);
                } else {
                    notify = Toast.makeText(context.getApplicationContext(), "This door is already closed", Toast.LENGTH_SHORT);
                }
                new CountDownTimer(700, 1000) {
                    public void onTick(long millisUntilFinished) {notify.show();}
                    public void onFinish() {notify.cancel();}
                }.start();
            }
        });

        return convertView;
    }
}
