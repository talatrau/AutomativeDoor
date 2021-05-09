package com.example.automativedoor.GUIControl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

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
        holder.txtName.setText(sensor.name);
        holder.aSwitch.setChecked(sensor.state);

        return convertView;
    }

    private class ViewHolder {
        TextView txtName;
        Switch aSwitch;
        Button button;
    }
}