package com.example.sean.lookitup;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by amt on 3/11/15.
 */
public class DictionaryHelper extends SQLiteOpenHelper {

    public static final String DICTIONARY_TABLE_NAME = "dictionary";
    public static final String KEY_TERM = "term";
    public static final String KEY_DEFINITION = "definition";
    public static final String DATABASE_NAME = "dictionary";
    public static final int DATABASE_VERSION = 1;

    public DictionaryHelper( Context context ){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String DICTIONARY_TABLE_CREATE =
            "CREATE TABLE " + DICTIONARY_TABLE_NAME +
                    " (" + KEY_TERM + " TEXT, " + KEY_DEFINITION + " TEXT);";

    @Override
    public void onCreate( SQLiteDatabase db ) {
        db.execSQL( DICTIONARY_TABLE_CREATE );
    }

    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVer, int newVer ) {
        db.execSQL( "DROP TABLE IF EXISTS " + DICTIONARY_TABLE_NAME );
        onCreate( db );
    }
}