package gov.multiversal.tasks4tigers2;

/* by Dave Small
 * April 2015
 */

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import gov.multiversal.tasks4tigers2.contentprovider.TaskContentProvider;
import gov.multiversal.tasks4tigers2.db.TaskTable;

/*
 * TaskActivity displays the existing task items
 * in a list sorted by completion, priority, & order added
 *
 * short clicking any task toggles it's "completed" status
 * long clicking a completed task deletes it from the list
 * long clicking an incomplete task allows one to edit it
 */

public class TaskMainActivity
        extends ListActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    // private Cursor cursor;
    private TaskCursorAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_main);

        ListView lv = getListView();
        lv.setDividerHeight(2);
        fillData();

        lv.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view,
                                           int pos, long id) {

                if ( TaskCursorAdapter.isTaskCompleted( view ) )
                    deleteTask( id );
                else
                    updateTask( id );

                return true; // consume the click event
            }

            private void deleteTask( long id ) {
                Uri taskUri = Uri.parse( TaskContentProvider.CONTENT_URI + "/" + id );
                getContentResolver().delete( taskUri, null, null );
            }

            private void updateTask( long id ) {
                Intent i = new Intent( TaskMainActivity.this, TaskDetailActivity.class );
                Uri todoUri = Uri.parse( TaskContentProvider.CONTENT_URI + "/" + id );
                i.putExtra( TaskContentProvider.CONTENT_ITEM_TYPE, todoUri );

                startActivity(i);
            }
        });
    }

    // populate the list with task data from the db
    private void fillData() {

        getLoaderManager().initLoader(0, null, this);
        adapter = new TaskCursorAdapter( this, // context
                                         null, // cursor
                                         0     // flags
        );

        setListAdapter( adapter );
    }


    // Opens the second activity if an entry is clicked
    @Override
    protected void onListItemClick( ListView l, View v, int position, long id ) {
        super.onListItemClick(l, v, position, id);

        ContentValues values = new ContentValues();
        values.put( TaskTable.COLUMN_COMPLETED,
                    TaskCursorAdapter.toggleCompleted( v ));

        Log.v("short clicked","position: " + position);
        Uri taskUri = Uri.parse( TaskContentProvider.CONTENT_URI + "/" + id );
        getContentResolver().update( taskUri, values, null, null );
    }

    public void addTaskButtonHandler( View v ) {
        Intent i = new Intent(this, TaskDetailActivity.class);
        startActivity(i);
        Log.d("TaskMainActivity", "addTaskHandler() finished");
    }

    /****************************************
    ** LoaderManager.LoaderCallbacks
    *****************************************/

    // creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        CursorLoader cursorLoader
                = new CursorLoader( this,
                                    TaskContentProvider.CONTENT_URI,
                                    TaskCursorAdapter.PROJECTION,
                                    null,
                                    null,
                                    TaskCursorAdapter.ORDER_BY );

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // data is not available anymore, delete reference
        adapter.swapCursor(null);
    }

}