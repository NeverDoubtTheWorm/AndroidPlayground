package com.example.sean.taskfortigers.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sean on 4/8/2015.
 */
public class TaskDBHelper extends SQLiteOpenHelper {
    static private final String DB_NAME = "task4tigers.db";
    static private final int DB_Version = 1;
    public TaskDBHelper( Context context ) {
        super( context, DB_NAME, null, DB_Version );
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        TaskTable.onCreate(db);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TaskTable.onUpgrade(db, oldVersion, newVersion);
    }
}
