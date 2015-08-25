package gov.multiversal.tasks4tigers2;

/* by Dave Small
 * April 2015
 */

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import gov.multiversal.tasks4tigers2.contentprovider.TaskContentProvider;
import gov.multiversal.tasks4tigers2.db.TaskTable;

/*
 * TaskDetailActivity: create new or edit an existing task
 */

public class TaskDetailActivity extends Activity {
    private static String[] PROJECTION = { TaskTable.COLUMN_PRIORITY,
                                           TaskTable.COLUMN_DETAILS,
                                           TaskTable.COLUMN_SUMMARY,
                                           TaskTable.COLUMN_COMPLETED
                                         };
    private Spinner mPriority;
    private EditText mSummaryText;
    private EditText mDetailsText;
    private int mCompleted = 0;

    private Uri mTaskUri;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_task_detail);

        mPriority  = (Spinner)  findViewById(R.id.priority);
        mSummaryText = (EditText) findViewById(R.id.task_summary_editor);
        mDetailsText = (EditText) findViewById(R.id.task_details_editor);

        Button doneButton = (Button) findViewById(R.id.task_confirm_button);

        Bundle extras = getIntent().getExtras();

        // check from the saved Instance
        mTaskUri = (bundle == null) ? null : (Uri) bundle
                .getParcelable(TaskContentProvider.CONTENT_ITEM_TYPE);

        // Or passed from the other activity
        if (extras != null) {
            mTaskUri = extras
                    .getParcelable(TaskContentProvider.CONTENT_ITEM_TYPE);

            fillData( mTaskUri );
        }

        doneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(mSummaryText.getText().toString())) {
                    makeToast();
                } else {
                    Log.d("TaskDetailActivity", "done button clicked");
                    setResult(RESULT_OK);
                    finish();  // will call onPause(), which calls saveState()
                }
            }

        });
    }

    private void fillData(Uri uri) {
        Cursor cursor = getContentResolver().query( uri, 
                                                    PROJECTION, 
                                                    null, null, null);
        // if editing an existing task
        if (cursor != null) {
            cursor.moveToFirst();

            // set the Spinner to the task's priority
            int priority = cursor.getInt(cursor
                    .getColumnIndexOrThrow(TaskTable.COLUMN_PRIORITY));
            

            // fill the EditTexts with stored values
            mSummaryText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(TaskTable.COLUMN_SUMMARY)));
            mDetailsText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(TaskTable.COLUMN_DETAILS)));

            mCompleted = cursor.getInt( cursor
                    .getColumnIndexOrThrow(TaskTable.COLUMN_COMPLETED));

            // always close the cursor
            cursor.close();
        }
    }

    protected void onSaveInstanceState( Bundle outState ) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable( TaskContentProvider.CONTENT_ITEM_TYPE, mTaskUri );
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    private void saveState() {
        Log.d("TaskDetailActivity", "saveState() entered");
        int priority = mPriority.getSelectedItemPosition();
        String summary = mSummaryText.getText().toString();
        String description = mDetailsText.getText().toString();

        // save only if there is a summary

        if ( summary.length() == 0 ) {
            Log.d("TaskDetailActivity", "saveState() failed--needs task summary");
            return;
        }

        ContentValues values = new ContentValues();
        values.put(TaskTable.COLUMN_PRIORITY, priority);
        values.put(TaskTable.COLUMN_SUMMARY, summary);
        values.put(TaskTable.COLUMN_DETAILS, description);
        values.put(TaskTable.COLUMN_COMPLETED, mCompleted);

        if (mTaskUri == null) {
            // add new task to list
            mTaskUri = getContentResolver().insert( TaskContentProvider.CONTENT_URI, values );
        } else {
            // update existing task
            getContentResolver().update(mTaskUri, values, null, null );
        }
    }

    private void makeToast() {
        Toast.makeText( TaskDetailActivity.this,
                        "Please add a task summary",
                        Toast.LENGTH_LONG ).show();
    }
}
