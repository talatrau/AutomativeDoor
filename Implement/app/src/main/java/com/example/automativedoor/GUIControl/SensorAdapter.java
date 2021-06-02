package com.example.automativedoor.GUIControl;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.automativedoor.Control.MQTTServer;
import com.example.automativedoor.Control.UserController;
import com.example.automativedoor.EntityClass.Sensor;
import com.example.automativedoor.R;

import java.util.List;

public class SensorAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Sensor> sensors;

    public SensorAdapter(Context context, int layout, List<Sensor> sensors) {
        this.context = context;
        this.layout = layout;
        this.sensors = sensors;
    }

    @Override
    public int getCount() {
        return sensors.size();
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
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder = new ViewHolder();

            holder.txtName = (TextView) convertView.findViewById(R.id.sensor_name);
            holder.aSwitch = (Switch) convertView.findViewById(R.id.sensor_state);
            holder.button = (Button) convertView.findViewById(R.id.sensor_schedule);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Sensor sensor = sensors.get(position);
        holder.txtName.setText(sensor.getName());
        holder.aSwitch.setChecked(sensor.getState());

        holder.aSwitch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (holder.aSwitch.isChecked()) { UserController.getInstance().turnOnSensor(position); }
                else UserController.getInstance().turnOffSensor(position);
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
                        sensors.get(position).updateName(name);
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
        Switch aSwitch;
        Button button;
    }
}
