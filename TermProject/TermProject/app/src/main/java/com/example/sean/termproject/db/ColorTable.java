package com.example.sean.termproject.db;

/* by Dave Small
 * April 2015
 */

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;

public class ColorTable {
    // Column names
    public static final String TABLE_COLOR = "Colors";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_RGB_HEX = "rgbhex";
    public static final String COLUMN_HUE = "hue";
    public static final String COLUMN_SATURATION = "saturation";
    public static final String COLUMN_VALUE = "value";

    public static final String SOURCE_FILE_NAME = "colorDBInserts.txt";

    // SQL statement to create the table
    private static final String DATABASE_CREATE = "create table "
            + TABLE_COLOR
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_RGB_HEX + " text not null, "
            + COLUMN_HUE + " integer not null, "
            + COLUMN_SATURATION + " integer not null, "
            + COLUMN_VALUE + " integer not null "
            + ");";

    private static HashSet<String> VALID_COLUMN_NAMES;

    public static void onCreate( SQLiteDatabase sqLiteDatabase ) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade( SQLiteDatabase sqLiteDatabase,
                                  int oldVersion,
                                  int newVersion ) {
        Log.d( ColorTable.class.getName(),
                "Upgrading database from version "
                        + oldVersion + " to " + newVersion
                        + ", which destroyed all existing data");

        sqLiteDatabase.execSQL( "DROP TABLE IF EXISTS " + TABLE_COLOR );
        onCreate( sqLiteDatabase );

        Log.d( "TableTask.onUpgrade()", "complete");
    }

    static {
        String[] validNames = {
            TABLE_COLOR,
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_RGB_HEX,
            COLUMN_HUE,
            COLUMN_SATURATION,
            COLUMN_VALUE
        };

        VALID_COLUMN_NAMES = new HashSet<String>(Arrays.asList(validNames));
    }

    public static void validateProjection ( String[] projection ) {
        if ( projection != null ) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));

            // check if all columns which are requested are available
            if ( !VALID_COLUMN_NAMES.containsAll( requestedColumns ) ) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }

    public static void createFromFile( SQLiteDatabase sqLiteDatabase,
                                       Context context ) {
        BufferedReader reader = null;
        try {
            AssetManager am = context.getAssets();
            reader = new BufferedReader(
                    new InputStreamReader(
                            am.open( SOURCE_FILE_NAME )
                    )
            );
            String getLine;

            sqLiteDatabase.beginTransaction();
            while ( ( getLine = reader.readLine() ) != null ) {
                sqLiteDatabase.execSQL( getLine );
            }
            sqLiteDatabase.setTransactionSuccessful();

        } catch ( IOException e ) {
            Log.e( "ColorTable" ,"ERROR Reading from file " + e.toString() );
        } finally {
            if ( reader != null ) {
                try {
                    reader.close();
                } catch ( IOException e ) {
                    Log.e( "ColorTable" ,"ERROR when closing buffered reader " + e.toString() );
                }
            }

            sqLiteDatabase.endTransaction();
        }
    }
}
