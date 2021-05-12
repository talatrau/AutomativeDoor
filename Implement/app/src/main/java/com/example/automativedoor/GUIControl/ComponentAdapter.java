package com.example.automativedoor.GUIControl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.automativedoor.EntityClass.Component;
import com.example.automativedoor.R;

import java.util.List;

public class ComponentAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Component> components;

    public ComponentAdapter(Context context, int layout, List<Component> components) {
        this.context = context;
        this.layout = layout;
        this.components = components;
    }

    @Override
    public int getCount() {
        return components.size();
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
            holder = new ViewHolder();

            holder.txtName = (TextView) convertView.findViewById(R.id.component_name);
            holder.txtState = (TextView) convertView.findViewById(R.id.component_state);
            holder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.component_layout);
            convertView.setTag(holder);
        } else {
            holder = (ComponentAdapter.ViewHolder) convertView.getTag();
        }

        Component component = components.get(position);
        holder.txtName.setText(component.getName());
        if (component.getState()) {
            holder.txtState.setText("ON");
            holder.relativeLayout.setBackgroundResource(R.drawable.component_on);
        } else {
            holder.txtState.setText("OFF");
            holder.relativeLayout.setBackgroundResource(R.drawable.component_off);
        }

        return convertView;
    }

    private class ViewHolder {
        TextView txtName;
        TextView txtState;
        RelativeLayout relativeLayout;
    }
}
