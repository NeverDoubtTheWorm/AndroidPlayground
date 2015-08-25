package com.example.sean.termproject.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by Sean Thomas on 4/23/2015.
 */
public class ColorContentProvider extends ContentProvider {
    // database
    private ColorDBHelper database;

    private static final String AUTHORITY
            = "com.example.sean.termproject.provider";

    private static final String BASE_PATH = "colors";

    public static final String CONTENT_TYPE
            = ContentResolver.CURSOR_DIR_BASE_TYPE + "/colors";

    public static final String CONTENT_ITEM_TYPE
            = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/color";

    public static final String CONTENT_URI_PREFIX
            = "content://" + AUTHORITY + "/" + BASE_PATH + "/";

    public static final Uri CONTENT_URI
            = Uri.parse( "content://" + AUTHORITY + "/" + BASE_PATH );

    // Uri matcher
    private static final UriMatcher sURIMatcher = new UriMatcher( UriMatcher.NO_MATCH );
    private static final int COLORS = 1;
    private static final int COLOR_ID = 2;
    static {
        sURIMatcher.addURI( AUTHORITY, BASE_PATH, COLORS );
        sURIMatcher.addURI( AUTHORITY, BASE_PATH + "/#", COLOR_ID );
    }

    @Override
    public boolean onCreate() {
        database = new ColorDBHelper( getContext() );
        return false;
    }

    @Override
    public Cursor query( Uri uri,
                         String[] projection,
                         String selection,
                         String[] selectionArgs,
                         String sortOrder ) {
        // check if the caller has requested a column which does not exists
        ColorTable.validateProjection( projection );

        // Using SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables( ColorTable.TABLE_COLOR );

        switch ( sURIMatcher.match(uri) ) {
            case COLORS:
                break;
            case COLOR_ID:
                // add the task ID to the original query
                queryBuilder.appendWhere( ColorTable.COLUMN_ID + "=" + uri.getLastPathSegment() );
                break;
            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query( db, projection, selection,
                selectionArgs, null, null, sortOrder);

        // notify listeners
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
