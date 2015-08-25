package gov.multiversal.tasks4tigers2;

/* by Dave Small
 * April 2015
 */

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import gov.multiversal.tasks4tigers2.db.TaskTable;


public class TaskCursorAdapter extends CursorAdapter {

    // Fields from the database (projection)
    // Must include the _id column for the adapter to work
    static private final int ID = 0;
    static private final int SUMMARY = 1;
    static private final int COMPLETED = 2;
    static private final int PRIORITY = 3;

    static public final String[] PROJECTION
            = new String[] { TaskTable.COLUMN_ID,
                             TaskTable.COLUMN_SUMMARY,
                             TaskTable.COLUMN_COMPLETED,
                             TaskTable.COLUMN_PRIORITY
                           };

    static public final String ORDER_BY
            = TaskTable.COLUMN_COMPLETED + "," +
              TaskTable.COLUMN_PRIORITY + "," +
              TaskTable.COLUMN_ID;

    static private final int[] PRIORITY_COLOR;
    static {
        PRIORITY_COLOR = new int[] {
                Color.parseColor( "#ffeeee" ),
                Color.parseColor( "#ffffee" ),
                Color.parseColor( "#eeffee" ),
                Color.parseColor( "#eeeeff" )
        };
    }

    static private final int TRUE = 1;

    static private class ViewHolder {
        ImageView icon;
        TextView label;
        boolean isCompleted;
    }

    private LayoutInflater mInflater;

    public TaskCursorAdapter( Context context, Cursor cursor, int flags ) {
        super(context, cursor, flags);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View row = mInflater.inflate(R.layout.task_row, parent, false);

        // cache the row's Views in a ViewHolder
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.icon = (ImageView) row.findViewById( R.id.icon );
        viewHolder.label = (TextView) row.findViewById( R.id.label );
        row.setTag( viewHolder );

        return row;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = updateViewHolderValues(view, cursor);
        decorateView( viewHolder, cursor );
    }

    private ViewHolder updateViewHolderValues(View view, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.label.setText( cursor.getString( SUMMARY ) );
        viewHolder.isCompleted = ( cursor.getInt(COMPLETED) == TRUE );

        return viewHolder;
    }

    private void decorateView( ViewHolder task, Cursor cursor ) {
        // set the text styling based on the the completion status
        if ( task.isCompleted ) {
            // strike-thru the text of isCompleted items
            task.label.setPaintFlags(
                    task.label.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG );

        } else {
            // clear the strike-thru flag (may have been set if view is recycled)
            task.label.setPaintFlags(
                    task.label.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG );
        }

        // set the background color based on the priority
        int priority = cursor.getInt( PRIORITY );
        if ( 0 <= priority && priority <= 2 )
            task.label.setBackgroundColor( PRIORITY_COLOR[priority] );
    }


    public static int toggleCompleted( View view ) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.isCompleted = ! viewHolder.isCompleted;

        return viewHolder.isCompleted ? 1 : 0;
    }

    public static boolean isTaskCompleted( View view ) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        return viewHolder.isCompleted;
    }
}
