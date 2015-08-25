package gov.multiversal.tasks4tigers2.db;

/* by Dave Small
 * April 2015
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TigerTaskDBHelper
        extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tigertasks.db";
    private static final int DATABASE_VERSION = 1;

    public TigerTaskDBHelper( Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate( SQLiteDatabase database ) {
        TaskTable.onCreate(database);
    }

    @Override
    public void onUpgrade( SQLiteDatabase database,
                           int oldVersion,
                           int newVersion) {
        TaskTable.onUpgrade( database, oldVersion, newVersion );
    }

}

