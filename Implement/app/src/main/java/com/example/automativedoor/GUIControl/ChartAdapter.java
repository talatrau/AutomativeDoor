package com.example.automativedoor.GUIControl;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import androidx.annotation.RequiresApi;

import com.example.automativedoor.Control.UserController;
import com.example.automativedoor.EntityClass.SensorHis;
import com.example.automativedoor.EntityClass.ServoHis;
import com.example.automativedoor.EntityClass.SpeakerHis;
import com.example.automativedoor.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.time.LocalDate;
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
            for (int i = 0; i < servoHisList[d].size(); i++){
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
            lineDataSet.setColor(Color.GREEN);
            lineDataSet.setLineWidth(3);
            ArrayList<ILineDataSet> dataSets= new ArrayList<>();
            dataSets.add(lineDataSet);
            LineData my_data = new LineData(dataSets);
            my_data.setValueTextColor(Color.RED);
            my_data.setValueTextSize(14);


            LineChart lineChart = convertView.findViewById(R.id.linechart);
            lineChart.setData(my_data);
            lineChart.invalidate();

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setValueFormatter(new MyAxisValueFormatter());
            lineChart.getLegend().setTextColor(Color.RED);

        } else if (typ == 2){
            ArrayList<Entry> data = getListEntries(servoChartData[position]);
            LineDataSet lineDataSet = new LineDataSet(data, "Servo");
            lineDataSet.setColor(Color.GREEN);
            lineDataSet.setLineWidth(3);
            ArrayList<ILineDataSet> dataSets= new ArrayList<>();
            dataSets.add(lineDataSet);
            LineData my_data = new LineData(dataSets);
            my_data.setValueTextColor(Color.RED);
            my_data.setValueTextSize(14);


            LineChart lineChart = convertView.findViewById(R.id.linechart);
            lineChart.setData(my_data);
            lineChart.invalidate();

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setValueFormatter(new MyAxisValueFormatter());
            lineChart.getLegend().setTextColor(Color.RED);
//            lineChart.setBackgroundColor(Color.YELLOW);



            int[]  modeCount  = UserController.getInstance().servoModeCount[position];
            PieChart pieChart = convertView.findViewById(R.id.pieChart);

            List<PieEntry> value = new ArrayList<>();
            value.add(new PieEntry(modeCount[0], "Welcome Mode"));
            value.add(new PieEntry(modeCount[1], "Anti theft Mode"));

            PieDataSet pieDataSet = new PieDataSet(value, "");
            PieData pieData = new PieData(pieDataSet);

            pieData.setDrawValues(true);
            pieData.setValueFormatter(new PercentFormatter(pieChart));
            pieData.setValueTextSize(12f);
            
            pieChart.setData(pieData);
            pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
            pieChart.getLegend().setTextColor(Color.WHITE);

            pieChart.setDrawHoleEnabled(true);
            pieChart.setUsePercentValues(true);
            pieChart.setEntryLabelTextSize(10f);
            pieChart.setEntryLabelColor(Color.BLACK);
            pieChart.setDrawEntryLabels(false);
            pieChart.setCenterText("History each mode");
            pieChart.setCenterTextSize(20f);
            pieChart.getDescription().setEnabled(false);


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
    private class MyAxisValueFormatter extends ValueFormatter {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public String getFormattedValue(float value) {
            Log.wtf("Day", String.valueOf(value));
            LocalDate time = LocalDate.now().minusDays((long) (7-value));
            time.getDayOfMonth();
            return time.getDayOfMonth() + "/" + time.getMonth().toString().toLowerCase();
        }

        //        @Override
//        public String getFormattedValue(float value, AxisBase axis) {
//            return "Day " + String.valueOf(value); //LocalDate.now().minusDays((long) (value)).toString()
//        }
    }
}
