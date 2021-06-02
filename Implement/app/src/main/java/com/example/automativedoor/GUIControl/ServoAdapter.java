package com.example.automativedoor.GUIControl;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.automativedoor.Control.MQTTServer;
import com.example.automativedoor.Control.SensorService;
import com.example.automativedoor.Control.UserController;
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
    }   // dont use

    @Override
    public long getItemId(int position) {
        return 0;
    }       // dont use

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder = new ViewHolder();

            holder.txtName = (TextView) convertView.findViewById(R.id.servo_name);
            holder.txtID = (TextView) convertView.findViewById(R.id.servo_id);
            holder.open = (Button) convertView.findViewById(R.id.servo_open);
            holder.close = (Button) convertView.findViewById(R.id.servo_close);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Servo servo = servos.get(position);
        holder.txtName.setText(servo.getName());
        holder.txtID.setText(servo.getDeviceID());

        holder.open.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                holder.open.startAnimation(AnimationUtils.loadAnimation(context, R.anim.bounce));
                Toast notify;
                if (UserController.getInstance().openDoor(position)) {
                    notify = Toast.makeText(context.getApplicationContext(), "Door opened", Toast.LENGTH_SHORT);
                } else {
                    notify = Toast.makeText(context.getApplicationContext(), "This door is already opened", Toast.LENGTH_SHORT);
                }
                new CountDownTimer(700, 1000) {
                    public void onTick(long millisUntilFinished) {notify.show();}
                    public void onFinish() {notify.cancel();}
                }.start();

            }
        });

        holder.close.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                holder.close.startAnimation(AnimationUtils.loadAnimation(context, R.anim.bounce));
                Toast notify;
                if (UserController.getInstance().closeDoor(position)) {
                    notify = Toast.makeText(context.getApplicationContext(), "Door closed", Toast.LENGTH_SHORT);
                } else {
                    notify = Toast.makeText(context.getApplicationContext(), "This door is already closed", Toast.LENGTH_SHORT);
                }
                new CountDownTimer(700, 1000) {
                    public void onTick(long millisUntilFinished) {notify.show();}
                    public void onFinish() {notify.cancel();}
                }.start();

            }
        });

        holder.txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Set Device Name");
                final EditText input = new EditText(context);
                builder.setView(input);
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = input.getText().toString();
                        servos.get(position).updateName(name);
                        holder.txtName.setText(name);
                    }
                });

                builder.setNegativeButton("Dismiss", null);
                builder.create().show();
            }
        });

        return convertView;
    }

    private class ViewHolder {
        TextView txtName;
        TextView txtID;
        Button open;
        Button close;
    }
}
