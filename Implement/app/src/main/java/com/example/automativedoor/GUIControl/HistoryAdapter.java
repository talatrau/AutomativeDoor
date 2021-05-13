package com.example.automativedoor.GUIControl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.automativedoor.EntityClass.History;
import com.example.automativedoor.R;

import org.w3c.dom.Text;

import java.util.List;

public class HistoryAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    List<History> listHistory;

    public HistoryAdapter(Context context, int layout, List<History> listHistory) {
        this.context = context;
        this.layout = layout;
        this.listHistory = listHistory;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout, null);
        History history = listHistory.get(position);

        TextView activity = convertView.findViewById(R.id.activity);
        TextView date = convertView.findViewById(R.id.date);
        TextView time = convertView.findViewById(R.id.time);

//        activity.setText();
        return null;
    }
}
