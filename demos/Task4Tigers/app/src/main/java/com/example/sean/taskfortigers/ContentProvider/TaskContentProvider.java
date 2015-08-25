package com.example.sean.taskfortigers.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.example.sean.taskfortigers.db.TaskDBHelper;
import com.example.sean.taskfortigers.db.TaskTable;

import java.util.Arrays;
import java.util.HashSet;

public class TaskContentProvider extends ContentProvider {

    private TaskDBHelper db;

    static private final String AUTHORITY
            = "com.example.sean.taskfortigers.provider";
    static private final String BASE_PATH
            = "tasks";
    static public final String CONTENT_TYPE
            = ContentResolver.CURSOR_DIR_BASE_TYPE + "/tasks";
    static public final String CONTENT_ITEM_TYPE
            = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/task";
    public static final Uri CONTENT_URI
            = Uri.parse( "content://" + AUTHORITY + "/" + BASE_PATH );

    // Setup the URIMatcher
    static private final UriMatcher sURIMatcher
            = new UriMatcher( UriMatcher.NO_MATCH ); // By Default this thing matches nothing
    static private final int TASKS = 1; // number pulled out of air
    static private final int TASK_ID = 2; // number pulled out of air
    static { // executes as class is loaded
        // Only supposed exist when class is loaded
        sURIMatcher.addURI( AUTHORITY, BASE_PATH, TASKS );
        sURIMatcher.addURI( AUTHORITY, BASE_PATH + "/#", TASK_ID );
    }

    @Override
    public boolean onCreate() {
        db = new TaskDBHelper( getContext() );
        return false;
    }

    @Override
    public Cursor query(Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        checkColumns( projection );   // verify that all the requested columns exist

        queryBuilder.setTables( TaskTable.TASK_TABLE );

        int uriType = sURIMatcher.match( uri );
        switch ( uriType ) {
            case TASKS:
                break;
            case TASK_ID:
                queryBuilder.appendWhere( TaskTable.COLUMN_ID
                                        + "=" + uri.getLastPathSegment() );
                break;
            default:
                throw new IllegalArgumentException( "Unknown URI: " + uri );
        }

        SQLiteDatabase wdb = db.getWritableDatabase();
        Cursor cursor = queryBuilder.query( wdb,
                                            projection,
                                            selection,
                                            selectionArgs,
                                            null,
                                            null,
                                            sortOrder );
        cursor.setNotificationUri( getContext().getContentResolver(),
                                   uri );

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert( Uri uri,
                       ContentValues values ) {
        //what kind of URI where we given
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase wdb = db.getWritableDatabase();
        long id;
        switch ( uriType ){
            case TASKS:
                id = wdb.insert(TaskTable.TASK_TABLE, null, values );
                break;
            default:
                throw new IllegalArgumentException( "Unknown URI: " + uri );
        }
        getContext().getContentResolver().notifyChange( uri, null );

        return Uri.parse( BASE_PATH + "/" + id );
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update( Uri uri,
                       ContentValues values,
                       String selection,
                       String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase wdb = db.getWritableDatabase();
        int rowsUpdated = 0;
        switch ( uriType ){
            case TASKS:

                break;
            case TASK_ID:

                break;
            default:
                throw new IllegalArgumentException( "Unknown URI: " + uri );
        }

        return 0;
    }

    private void checkColumns( String[] projection ){
        String[] available = { TaskTable.COLUMN_ID,
                               TaskTable.COLUMN_PRIORITY,
                               TaskTable.COLUMN_SUMMARY,
                               TaskTable.COLUMN_DESCRIPTION };
        if( projection != null ){
            HashSet<String> requestedColumns = new HashSet<String>( Arrays.asList(projection) );
            HashSet<String> availableColumns = new HashSet<String>( Arrays.asList(available) );
            if( ! availableColumns.containsAll( requestedColumns ) ){
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}
