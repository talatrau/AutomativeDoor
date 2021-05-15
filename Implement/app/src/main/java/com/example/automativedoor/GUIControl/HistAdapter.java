package com.example.automativedoor.GUIControl;

import android.content.Context;
import android.os.Build;
import android.util.Log;
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
    public List<ServoHis> listServoHis;
    public List<SensorHis> listSensorHis;
    public List<SpeakerHis> listSpeakerHis;
    private int type = -1; // 0: sensor, 1: speaker, 2: servo


    public HistAdapter(Context context, int layout) {
        this.context = context;
        this.layout = layout;
        listServoHis = null;
        listSensorHis = null;
        listSpeakerHis = null;
    }

    public void setListServoHis(List<ServoHis> listServoHis) {
        this.listServoHis = listServoHis;
        type = 2;
    }

    public void setListSensorHis(List<SensorHis> listSensorHis) {
        this.listSensorHis = listSensorHis;
        type = 0;
    }

    public void setListSpeakerHis(List<SpeakerHis> listSpeakerHis) {
        this.listSpeakerHis = listSpeakerHis;
        type = 1;
    }

    @Override
    public int getCount() {
        return listSensorHis.size();
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
        List<String> times = null;

        if (type == 0)
            times = getData(listSensorHis.get(position));
        else if (type == 2)
            times = getData(listServoHis.get(position));

        if (times.size() == 1){
            if (type != 1)
                convertView = oneLine(convertView, times.get(0));

        } else if (times.size() == 2){
            convertView = twoLine(convertView, times.get(0), times.get(1));
        }


        return convertView;
    }
    private List<String> getData(SensorHis history){
        List<String> ret = new ArrayList<>();
        ret.add(history.getStartTime());
        if (history.getEndTime() != "")
            ret.add(history.getEndTime());
        return ret;
    }
    private List<String> getData(ServoHis history){
        List<String> ret = new ArrayList<>();
        ret.add(history.getStartTime());
        if (history.getEndTime() != "")
            ret.add(history.getEndTime());
        return ret;
    }

    private View oneLine(View convertView, String sTime){
        TextView date = convertView.findViewById(R.id.date);
        TextView time = convertView.findViewById(R.id.time);

        date.setText(sTime.substring(0, 10));
        time.setText(sTime.substring(10));
        return convertView;
    }
    private View twoLine(View convertView, String sTime, String eTime){
        TextView dateStart = convertView.findViewById(R.id.dateStart);
        TextView timeStart = convertView.findViewById(R.id.timeStart);

        TextView dateEnd = convertView.findViewById(R.id.dateEnd);
        TextView timeEnd = convertView.findViewById(R.id.timeEnd);

        dateStart.setText(sTime.substring(0, 10));
        timeStart.setText(sTime.substring(10));

        dateEnd.setText(eTime.substring(0, 10));
        timeEnd.setText(eTime.substring(10));
        return convertView;
    }
}
