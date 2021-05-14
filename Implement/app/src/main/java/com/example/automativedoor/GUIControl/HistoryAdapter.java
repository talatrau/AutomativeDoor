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

        TextView dateStart = convertView.findViewById(R.id.dateStart);
        TextView timeStart = convertView.findViewById(R.id.timeStart);

        TextView dateEnd = convertView.findViewById(R.id.dateEnd);
        TextView timeEnd = convertView.findViewById(R.id.timeEnd);



//        activity.setText();
        return null;
    }
}
