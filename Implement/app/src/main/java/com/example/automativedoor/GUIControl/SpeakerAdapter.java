package com.example.automativedoor.GUIControl;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.automativedoor.Control.MQTTServer;
import com.example.automativedoor.Control.UserController;
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

        holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                UserController.getInstance().setSpeaker(position, progressChangedValue);
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
                        speakers.get(position).updateName(name);
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
        SeekBar seekBar;
    }
}
