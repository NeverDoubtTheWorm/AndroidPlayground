package com.example.sean.lookitup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by amt on 3/11/15.
 */
public class DictionaryDAO {
    private DictionaryHelper mHelper;
    private SQLiteDatabase mDatabase;

    // dictionary table related constants.
    public final static String DICT_TABLE   = DictionaryHelper.DICTIONARY_TABLE_NAME;
    public final static String DICT_TERM    = DictionaryHelper.KEY_TERM;
    public final static String DICT_DEF     = DictionaryHelper.KEY_DEFINITION;


    public DictionaryDAO(Context context){
        mHelper   = new DictionaryHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }

    public long createDictionaryEntry( String term, String def ) {

        ContentValues values = new ContentValues();

        values.put( DICT_TERM, term );
        values.put( DICT_DEF, def );

        return mDatabase.insert( DICT_TABLE, null, values);
    }

    public Cursor fetchAllEntries() {

        Cursor cursor
                = mDatabase.query( true, DICT_TABLE,
                new String[] { DICT_TERM, DICT_DEF },
                null, null, null, null, null, null    );

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public long updateDictionaryEntry( String term, String def ) {

        ContentValues values = new ContentValues();

        values.put( DICT_TERM, term );
        values.put( DICT_DEF, def );

        return mDatabase.update(     DICT_TABLE, values,
                DICT_TERM + "=?", new String[] { term } );
    }

    public boolean deleteDictionaryEntry( String term ) {
        return mDatabase.delete(    DICT_TABLE, DICT_TERM + "=" + term, null ) > 0;
    }
}