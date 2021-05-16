package com.example.automativedoor.GUIControl;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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

    MQTTServer mqttServer;

    public SensorAdapter(Context context, int layout, List<Sensor> sensors, MQTTServer mqttServer) {
        this.context = context;
        this.layout = layout;
        this.sensors = sensors;
        this.mqttServer = mqttServer;
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
                if (holder.aSwitch.isChecked()) { UserController.getInstance().turnOnSensor(position, mqttServer); }
                else UserController.getInstance().turnOffSensor(position, mqttServer);
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
