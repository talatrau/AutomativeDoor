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

import com.example.automativedoor.Control.UserController;
import com.example.automativedoor.EntityClass.History;
import com.example.automativedoor.EntityClass.SensorHis;
import com.example.automativedoor.EntityClass.ServoHis;
import com.example.automativedoor.EntityClass.SpeakerHis;
import com.example.automativedoor.R;

import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HistAdapter extends BaseAdapter {
    private Context context;
    private int layout;
//    public List<ServoHis> listServoHis;
//    public List<SensorHis> listSensorHis;
//    public List<SpeakerHis> listSpeakerHis;
    private int type = -1; // 0: sensor, 1: speaker, 2: servo

    public ArrayList<String[]> listServoHis;
    public ArrayList<String[]> listSpeakerHis;
    public ArrayList<String[]> listSensorHis;

    public HistAdapter(Context context, int layout) {
        this.context = context;
        this.layout = layout;
        listServoHis = null;
        listSensorHis = null;
        listSpeakerHis = null;
    }

    public void setListServoHis(ArrayList<String[]> listServoHis) {
        this.listServoHis = listServoHis;
//        for (int i = 0; i < listServoHis.size(); i ++){
//            Log.wtf("Hoang", "Value: " + Arrays.toString(listServoHis.get(i)));
//        }
        type = 2;
    }

    public void setListSensorHis(ArrayList<String[]> listSensorHis) {
        Log.wtf("Hoang", "setListSensorHis start");
        this.listSensorHis = listSensorHis;
        for (int i = 0; i < listSensorHis.size(); i ++){
            Log.wtf("Hoang", "Value: " + Arrays.toString(listSensorHis.get(i)));
        }
        type = 0;
        Log.wtf("Hoang", "setListSensorHis end");

    }

    public void setListSpeakerHis(ArrayList<String[]> listSpeakerHis) {
        this.listSpeakerHis = listSpeakerHis;
        type = 1;
    }

    @Override
    public int getCount() {
        if (type == 2){
            return listServoHis.size();
        } else if (type == 1){
            return listSpeakerHis.size();
        }
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

        if (type == 1){
            String[] data = listSpeakerHis.get(position);
            Log.wtf("Hoang", "data adapter: " + Arrays.toString(data));

            TextView date = convertView.findViewById(R.id.date);
            TextView time = convertView.findViewById(R.id.time);
            TextView deviceID = convertView.findViewById(R.id.deviceID);

            date.setText(data[1].substring(0, 10));
            time.setText(data[1].substring(10));
            deviceID.setText(data[0]);

        }
        else if (type == 2){
            String[] data = listServoHis.get(position);
            Log.wtf("Hoang", "data adapter: " + Arrays.toString(data));

            TextView dateStart = convertView.findViewById(R.id.dateStart);
            TextView timeStart = convertView.findViewById(R.id.timeStart);

            TextView dateEnd = convertView.findViewById(R.id.dateEnd);
            TextView timeEnd = convertView.findViewById(R.id.timeEnd);
            TextView deviceID = convertView.findViewById(R.id.deviceID);

            dateStart.setText(data[1].substring(0, 10));
            timeStart.setText(data[1].substring(10));

            dateEnd.setText(data[2].substring(0, 10));
            timeEnd.setText(data[2].substring(10));
            deviceID.setText(data[0]);
        } else {
            String[] data = listSensorHis.get(position);
            Log.wtf("Hoang", "data adapter: " + Arrays.toString(data));

            TextView date = convertView.findViewById(R.id.date);
            TextView time = convertView.findViewById(R.id.time);
            TextView deviceID = convertView.findViewById(R.id.deviceID);

            date.setText(data[1].substring(0, 10));
            time.setText(data[1].substring(10));
            deviceID.setText(data[0]);
        }
//            times = getData(listServoHis.get(position));

//        if (times.size() == 1){
//            if (type != 1)
//                convertView = oneLine(convertView, times.get(0));
//
//        }


        return convertView;
    }

    private List<String> getData(SensorHis history){
        List<String> ret = new ArrayList<>();
//        ret.add(history.sTime);
//        if (history.eTime != "")
//            ret.add(history.eTime);
        return ret;
    }
//    private List<String> getData(ServoHis history){
//        List<String> ret = new ArrayList<>();
//        ret.add(history.oTime);
//        if (history.cTime != "")
//            ret.add(history.cTime);
//        return ret;
//    }

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
