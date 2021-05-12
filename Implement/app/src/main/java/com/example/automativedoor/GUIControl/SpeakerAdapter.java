package com.example.automativedoor.GUIControl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.automativedoor.EntityClass.Speaker;
import com.example.automativedoor.R;

import java.util.List;

public class SpeakerAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Speaker> speakers;

    public SpeakerAdapter(Context context, int layout, List<Speaker> speakers) {
        this.context = context;
        this.layout = layout;
        this.speakers = speakers;
    }

    @Override
    public int getCount() {
        return speakers.size();
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
        SpeakerAdapter.ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder = new ViewHolder();

            holder.txtName = (TextView) convertView.findViewById(R.id.speaker_name);
            holder.seekBar = (SeekBar) convertView.findViewById(R.id.speaker_seekbar);
            convertView.setTag(holder);
        } else {
            holder = (SpeakerAdapter.ViewHolder) convertView.getTag();
        }

        Speaker speaker = speakers.get(position);
        holder.txtName.setText(speaker.getName());
        holder.seekBar.setProgress(speaker.getVolume());

        return convertView;
    }

    private class ViewHolder {
        TextView txtName;
        SeekBar seekBar;
    }
}
