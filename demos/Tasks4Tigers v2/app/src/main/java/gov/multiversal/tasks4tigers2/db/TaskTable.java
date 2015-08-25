package gov.multiversal.tasks4tigers2.db;

/* by Dave Small
 * April 2015
 */

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.Arrays;
import java.util.HashSet;


public class TaskTable {
    // Column names
    public static final String TABLE_TASK = "task";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRIORITY = "priority";
    public static final String COLUMN_SUMMARY = "summary";
    public static final String COLUMN_DETAILS = "details";
    public static final String COLUMN_COMPLETED = "completed";

    // SQL statement to create the table
    private static final String DATABASE_CREATE = "create table "
            + TABLE_TASK
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_PRIORITY + " integer not null, "
            + COLUMN_SUMMARY + " text not null,"
            + COLUMN_DETAILS + " text not null, "
            + COLUMN_COMPLETED + " boolean not null check (" + COLUMN_COMPLETED + " in (0,1) )"
            + ");";

    private static HashSet<String> VALID_COLUMN_NAMES;

    public static void onCreate( SQLiteDatabase database ) {

        database.execSQL( DATABASE_CREATE );
    }

    public static void onUpgrade( SQLiteDatabase database,
                                  int oldVersion,
                                  int newVersion) {
        Log.d( TaskTable.class.getName(),
                "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which destroyed all existing data" );

        database.execSQL( "DROP TABLE IF EXISTS " + TABLE_TASK );
        onCreate( database );

        Log.d( "TableTask.onUpgrade()", "complete");
    }

    static {
        String[] validNames = {
                TABLE_TASK,
                COLUMN_ID,
                COLUMN_PRIORITY,
                COLUMN_SUMMARY,
                COLUMN_DETAILS,
                COLUMN_COMPLETED
        };

        VALID_COLUMN_NAMES = new HashSet<String>(Arrays.asList( validNames ));
    }

    public static void validateProjection(String[] projection) {

        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));

            // check if all columns which are requested are available
            if ( !VALID_COLUMN_NAMES.containsAll( requestedColumns ) ) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}

