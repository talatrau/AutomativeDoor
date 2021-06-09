package com.example.automativedoor.GUIControl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.automativedoor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TimerAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    JSONArray jsonArray;

    public TimerAdapter(Context context, int layout, JSONArray array) {
        this.context = context;
        this.layout = layout;
        this.jsonArray = array;
    }

    @Override
    public int getCount() {
        return jsonArray.length();
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
            holder = new TimerAdapter.ViewHolder();

            holder.txt_time = (TextView) convertView.findViewById(R.id.timer_time);
            holder.txt_label = (TextView) convertView.findViewById(R.id.timer_label);
            holder.txt_action = (TextView) convertView.findViewById(R.id.timer_action);
            convertView.setTag(holder);
        } else {
            holder = (TimerAdapter.ViewHolder) convertView.getTag();
        }

        try {
            JSONObject object = jsonArray.getJSONObject(position);
            int hour = object.getInt("hour");
            int min = object.getInt("minute");
            String hour_time = hour < 10 ? "0" + hour : String.valueOf(hour);
            String min_time = min < 10 ? "0" + min : String.valueOf(min);
            String label = object.get("label").toString();
            String action = object.get("action").toString();

            holder.txt_time.setText(hour_time + ":" + min_time);
            holder.txt_label.setText(label);
            holder.txt_action.setText("Action: " + action);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private class ViewHolder {
        TextView txt_time;
        TextView txt_label;
        TextView txt_action;
    }
}
