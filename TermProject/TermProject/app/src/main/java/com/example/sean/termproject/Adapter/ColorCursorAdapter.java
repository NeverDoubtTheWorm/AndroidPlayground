package com.example.sean.termproject.Adapter;

/* by Dave Small
 * April 2015
 */

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.sean.termproject.R;
import com.example.sean.termproject.db.ColorTable;


public class ColorCursorAdapter extends CursorAdapter {

    // Fields from the database (projection)
    // Must include the _id column for the adapter to work
    public static final int ID = 0;
    public static final int NAME = 1;
    public static final int RGB_HEX = 2;
    public static final int HUE = 3;
    public static final int SATURATION = 4;
    public static final int VALUE = 5;

    public static final String[] PROJECTION = new String[] {
            ColorTable.COLUMN_ID,
            ColorTable.COLUMN_NAME,
            ColorTable.COLUMN_RGB_HEX,
            ColorTable.COLUMN_HUE,
            ColorTable.COLUMN_SATURATION,
            ColorTable.COLUMN_VALUE,
    };

    private static class ColorViewHolder {
        TextView preview;
        TextView name;
        TextView details;
    }

    private LayoutInflater mInflator;
    private int itemCount = 0;

    public ColorCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        mInflator = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View v = mInflator.inflate( R.layout.named_color, viewGroup, false );

        ColorViewHolder viewHolder = new ColorViewHolder();
        viewHolder.preview = ( TextView ) v.findViewById( R.id.preview );
        viewHolder.name = ( TextView ) v.findViewById( R.id.color_name );
        viewHolder.details = ( TextView ) v.findViewById( R.id.color_details );

        v.setTag( viewHolder );
        itemCount++;

        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ColorViewHolder viewHolder = (ColorViewHolder) view.getTag();

        viewHolder.preview.setBackgroundColor(
                Color.parseColor(
                        cursor.getString( RGB_HEX )
        )  );
        viewHolder.name.setText( cursor.getString( NAME ) );
        viewHolder.details.setText(
                String.format(
                        "H = %3d     S = %3d     V = %3d",
                        cursor.getInt( HUE ),
                        cursor.getInt( SATURATION ),
                        cursor.getInt( VALUE )
        )  );
    }

    public boolean noResults() {
        return itemCount == 0;
    }
}
