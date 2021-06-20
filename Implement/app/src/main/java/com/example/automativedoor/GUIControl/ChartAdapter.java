package com.example.automativedoor.GUIControl;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.automativedoor.Control.UserController;
import com.example.automativedoor.EntityClass.SensorHis;
import com.example.automativedoor.EntityClass.ServoHis;
import com.example.automativedoor.EntityClass.SpeakerHis;
import com.example.automativedoor.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ChartAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    int typ = -1;
    int number_day = 0;
    Hashtable<String, Integer> hashMap;

    int [][] speakerChartData; // speaker warning
    int [][] servoChartData;
    int [][] sensorChartData;


    static int max_pos_current;

    int [][] servoPieData;


    public ChartAdapter(Context _context, int _layout, int _typ){
        context = _context;
        layout = _layout;
        typ = _typ;
    }

    public void setSpeaker(List<SpeakerHis>[] speakerHisList, int num_day){
        Log.wtf("Debug", "setSpeaker ran");
        number_day = num_day;
        hashMap = new Hashtable<String, Integer>();
        max_pos_current = 0;
        SpeakerHis tempSpeaker;

        speakerChartData = new int [UserController.getInstance().numberSpeaker][num_day];

        for (int d = 0; d < num_day; d ++){
            for (int i = 0; i < speakerHisList[d].size(); i ++){
                tempSpeaker = speakerHisList[d].get(i);
                if (! hashMap.containsKey(tempSpeaker.deviceID)){
                    hashMap.put(tempSpeaker.deviceID, max_pos_current++);
                }
                speakerChartData[hashMap.get(tempSpeaker.deviceID)][num_day - 1 - d] = tempSpeaker.time.size();
            }
        }
        for (int i = 0; i < num_day; i ++){
            Log.wtf("Debug", String.format("setSpeaker value: %d", speakerChartData[0][i]));
        }
    }


    public void setServo(List<ServoHis>[] servoHisList, int num_day){
        UserController.getInstance().servoModeCount = new int[UserController.getInstance().numberServo][2];
        Log.wtf("Debug", "setServo ran");
        number_day = num_day;
        hashMap = new Hashtable<String, Integer>();
        max_pos_current = 0;

        servoChartData = new int[UserController.getInstance().numberServo][num_day];

        ServoHis tempServo;
        for (int d = 0; d < num_day; d ++){
            Log.wtf("Hoang_Debug", String.format("day %d", d + 1));
            for (int i = 0; i < servoHisList[d].size(); i ++){
                tempServo = servoHisList[d].get(i);
                if (! hashMap.containsKey(tempServo.deviceID)){
                    hashMap.put(tempServo.deviceID, max_pos_current++);
                }
                servoChartData[hashMap.get(tempServo.deviceID)][num_day - 1 - d] = tempServo.getSize();

                Log.wtf("Hoang_Debug", String.format("device: %s", tempServo.deviceID));

                for (int t = 0; t < tempServo.getSize(); t ++){
                    String time = tempServo.getOTime(t);
                    int mode = Character.getNumericValue(time.charAt(time.length() - 1));
//                    Log.wtf("Hoang_Debug", String.format("mode: %d", mode));
                    UserController.getInstance().servoModeCount[hashMap.get(tempServo.deviceID)][mode - 1] += 1;
                }
            }
        }
        for (int i =0; i < UserController.getInstance().numberServo; i ++){
            Log.wtf("Debug_Hoang", String.format("i: %d, 0: %d, 1: %d", i, UserController.getInstance().servoModeCount[i][0], UserController.getInstance().servoModeCount[i][1]));
        }

        for (int i = 0; i < num_day; i ++){
            Log.wtf("Debug", String.format("setServo value: %d", servoChartData[0][i]));
        }

    }

    public void setSensor(List<SensorHis>[] sensorHisList, int num_day){
        Log.wtf("Debug", "setSensor ran");
        number_day = num_day;
        hashMap = new Hashtable<String, Integer>();
        max_pos_current = 0;

        sensorChartData = new int[UserController.getInstance().numberSensor][num_day];

        SensorHis tempSensor;
        for (int d = 0; d < num_day; d ++){
            for (int i = 0; i < sensorHisList[d].size(); i ++){
                tempSensor = sensorHisList[d].get(i);
                if (! hashMap.containsKey(tempSensor.deviceID)){
                    hashMap.put(tempSensor.deviceID, max_pos_current++);
                }
                sensorChartData[hashMap.get(tempSensor.deviceID)][num_day - 1 - d] = tempSensor.obstacle.size();
            }
        }

        for (int i = 0; i < num_day; i ++){
            Log.wtf("Debug", String.format("setSensor value: %d", sensorChartData[1][i]));
        }
    }

    @Override
    public int getCount() {
        if (typ == 1){
            return UserController.getInstance().numberSpeaker;
        } else if (typ == 0){
            return UserController.getInstance().numberSensor;
        }
        return UserController.getInstance().numberServo;
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout, null);

        if (typ == 1){
            ArrayList<Entry> data = getListEntries(speakerChartData[position]);
            LineDataSet lineDataSet = new LineDataSet(data, "Speaker");
            ArrayList<ILineDataSet> dataSets= new ArrayList<>();
            dataSets.add(lineDataSet);
            LineData my_data = new LineData(dataSets);

            LineChart lineChart = convertView.findViewById(R.id.linechart);
            lineChart.setData(my_data);
            lineChart.invalidate();

        } else if (typ == 2){
            ArrayList<Entry> data = getListEntries(servoChartData[position]);
            LineDataSet lineDataSet = new LineDataSet(data, "Servo");
            ArrayList<ILineDataSet> dataSets= new ArrayList<>();
            dataSets.add(lineDataSet);
            LineData my_data = new LineData(dataSets);

            LineChart lineChart = convertView.findViewById(R.id.linechart);
            lineChart.setData(my_data);
            lineChart.invalidate();

            int[]  modeCount  = UserController.getInstance().servoModeCount[position];
            PieChart pieChart = convertView.findViewById(R.id.pieChart);

            List<PieEntry> value = new ArrayList<>();
            value.add(new PieEntry(modeCount[0], "Welcome Mode"));
            value.add(new PieEntry(modeCount[1], "Anti theft Mode"));

            PieDataSet pieDataSet = new PieDataSet(value, "PieChart Name");
            PieData pieData = new PieData(pieDataSet);

            pieChart.setData(pieData);
            pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);


        } else {
            ArrayList<Entry> data = getListEntries(sensorChartData[position]);
            LineDataSet lineDataSet = new LineDataSet(data, "Sensor");
            ArrayList<ILineDataSet> dataSets= new ArrayList<>();
            dataSets.add(lineDataSet);
            LineData my_data = new LineData(dataSets);

            LineChart lineChart = convertView.findViewById(R.id.linechart);
            lineChart.setData(my_data);
            lineChart.invalidate();
        }
        return convertView;
    }

    private ArrayList<Entry> getListEntries(int[] data_plot){
        ArrayList<Entry> data = new ArrayList<Entry>();
        for (int i = 0; i < this.number_day; i ++){
            data.add(new Entry(i + 1, data_plot[i]));
        }
        return data;
    }
}
