package com.example.sean.termproject.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Locale;

/**
 * Created by Sean Thomas on 4/23/2015.
 */
public class ColorDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_PATH = "/data/data/com.example.sean.termproject/databases/";
    public static final String DATABASE_NAME = "color.db";
    public static final int DATABASE_VERSION = 1;

    public SQLiteDatabase dbSQLite;

    private Context mContext;

    public ColorDBHelper( Context context ) {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
        mContext = context;
    }

    @Override
    public void onCreate( SQLiteDatabase sqLiteDatabase ) {
        createDatabase( sqLiteDatabase );
    }

    @Override
    public void onUpgrade( SQLiteDatabase sqLiteDatabase,
                           int oldVersion,
                           int newVersion) {
        ColorTable.onUpgrade( sqLiteDatabase, oldVersion, newVersion );
    }

    private void createDatabase( SQLiteDatabase sqLiteDatabase  ) {
        SQLiteDatabase db = null;

        try {
            String databasePath = DATABASE_PATH + DATABASE_NAME;
            db = SQLiteDatabase.openDatabase( databasePath,
                                              null,
                                              SQLiteDatabase.OPEN_READWRITE );
            db.setLocale( Locale.getDefault() );
            db.setVersion(1);
        } catch ( SQLiteException e ) {
            Log.e( "ColorDBHelper", "database not found" );
        }

        boolean dbExists = ( db != null );

        if( dbExists ) {
            db.close();
        }


        if( !dbExists ) {

            ColorTable.onCreate(sqLiteDatabase);
            ColorTable.createFromFile(sqLiteDatabase, mContext);
        }
    }
}
