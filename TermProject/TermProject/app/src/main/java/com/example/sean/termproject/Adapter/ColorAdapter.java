package com.example.sean.termproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sean.termproject.R;
import com.example.sean.termproject.SwatchHelper.SwatchHelper;

import java.util.List;

/**
 * Created by Sean Thomas on 4/22/2015.
 */
public class ColorAdapter extends ArrayAdapter<SwatchHelper>{
    private final List<SwatchHelper> Swatches;

    public ColorAdapter(Context context,
                        List<SwatchHelper> resource) {
        super(context, 0, resource);
        Swatches = resource;
    }

    public static class ViewHolder {
        TextView preview;
//        TextView name;
//        TextView hexcode;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder; //cache views to avoid future lookup

        // check if existing View is being recycled; if not
        // inflate the view

        if (convertView == null){
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.color, parent, false);

            viewHolder.preview = (TextView) convertView.findViewById(R.id.preview);

            // cache the Views
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //Populate the (sub)views
        viewHolder.preview.setBackground(
                Swatches.get( position ).getGradient()
        );

        return convertView;
    }

    public SwatchHelper getSwatch( int position ) {
        return Swatches.get( position );
    }
}
