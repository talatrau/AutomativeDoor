package com.example.automativedoor.GUIControl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.automativedoor.EntityClass.Component;
import com.example.automativedoor.EntityClass.Sensor;
import com.example.automativedoor.EntityClass.Servo;
import com.example.automativedoor.EntityClass.Speaker;
import com.example.automativedoor.R;

import java.util.List;

public class ComponentAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Component> components;
    private List<Servo> servos;
    private List<Speaker> speakers;

    public ComponentAdapter(Context context, int layout, List<Component> components, List<Servo> servos, List<Speaker> speakers) {
        this.context = context;
        this.layout = layout;
        this.components = components;
        this.servos = servos;
        this.speakers = speakers;
    }

    @Override
    public int getCount() {
        return this.components.size();
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

            holder.txtName = (TextView) convertView.findViewById(R.id.component_name);
            holder.txtState = (TextView) convertView.findViewById(R.id.component_state);
            holder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.component_layout);
            convertView.setTag(holder);
        } else {
            holder = (ComponentAdapter.ViewHolder) convertView.getTag();
        }

        int speaker_index = 0;
        int servo_index = speakers.size() + speaker_index;

        if (position < servo_index) {
            Speaker speaker = speakers.get(position - speaker_index);
            holder.txtName.setText(speaker.getName());
            holder.txtState.setText(String.valueOf(speaker.getVolume()));
            holder.relativeLayout.setBackgroundResource(R.drawable.component_on);
        }
        else {
            Servo servo = servos.get(position - servo_index);
            holder.txtName.setText(servo.getName());
            if (servo.getState()) {
                holder.txtState.setText("ON");
                holder.relativeLayout.setBackgroundResource(R.drawable.component_on);
            } else {
                holder.txtState.setText("OFF");
                holder.relativeLayout.setBackgroundResource(R.drawable.component_off);
            }
        }

        return convertView;
    }

    private class ViewHolder {
        TextView txtName;
        TextView txtState;
        RelativeLayout relativeLayout;
    }
}
