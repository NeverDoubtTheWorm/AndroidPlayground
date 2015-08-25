package com.example.nick.x11colors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Nick on 3/27/2015.
 */
public class X11ColorAdapter extends ArrayAdapter<X11Color> {
    private final List<X11Color> colorsList;

    public X11ColorAdapter(Context context, List<X11Color> objects) {
        super(context, 0, objects);

        colorsList = objects;
    }

    public static class ViewHolder {
        TextView preview;
        TextView name;
        TextView hexcode;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder; //cache views to avoid future lookup

        // check if existing View is being recyled; if not
        // inflate the view

        if (convertView == null){
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.color, parent, false);

            viewHolder.preview = (TextView) convertView.findViewById(R.id.preview);
            viewHolder.name = (TextView) convertView.findViewById(R.id.color_name);
            viewHolder.hexcode = (TextView) convertView.findViewById(R.id.hex_code);

            // cache the Views
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //Populate the (sub)views
        X11Color color = getItem(position);
        viewHolder.preview.setBackgroundColor(color.getColorAsInt());
        viewHolder.name.setText(color.getName());
        viewHolder.hexcode.setText(color.getHexcode());

        return convertView;
    }

    public void remove(int position){
        colorsList.remove(position);
        notifyDataSetChanged();
    }
}
