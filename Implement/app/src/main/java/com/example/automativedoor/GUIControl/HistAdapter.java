package com.example.automativedoor.GUIControl;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.automativedoor.EntityClass.History;
import com.example.automativedoor.EntityClass.SensorHis;
import com.example.automativedoor.EntityClass.ServoHis;
import com.example.automativedoor.EntityClass.SpeakerHis;
import com.example.automativedoor.R;

import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HistAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    List<ServoHis> listServoHis;
    List<SensorHis> listSensorHis;
    List<SpeakerHis> listSpeakerHis;
    ArrayList listHistory;

    public HistAdapter(Context context, int layout, List<SensorHis> listHistory) {
        this.context = context;
        this.layout = layout;
        this.listSensorHis = listHistory;
    }


    @Override
    public int getCount() {
        return listHistory.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout, null);
        SensorHis history = listSensorHis.get(position);

        TextView dateStart = convertView.findViewById(R.id.dateStart);
        TextView timeStart = convertView.findViewById(R.id.timeStart);

        TextView dateEnd = convertView.findViewById(R.id.dateEnd);
        TextView timeEnd = convertView.findViewById(R.id.timeEnd);

        String sTime = history.getStartTime();
        String eTime = history.getEndTime();

        dateStart.setText(sTime.substring(0, 10));
        timeStart.setText(sTime.substring(10));

        dateEnd.setText(eTime.substring(0, 10));
        timeEnd.setText(eTime.substring(10));

        return convertView;
    }
}
