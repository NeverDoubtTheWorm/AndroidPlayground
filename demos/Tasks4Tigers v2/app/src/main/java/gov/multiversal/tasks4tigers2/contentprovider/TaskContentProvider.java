package gov.multiversal.tasks4tigers2.contentprovider;

/* by Dave Small
 * April 2015
 */

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.content.ContentResolver;
import android.content.UriMatcher;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.text.TextUtils;
import android.util.Log;

import gov.multiversal.tasks4tigers2.db.TigerTaskDBHelper;
import gov.multiversal.tasks4tigers2.db.TaskTable;

public class TaskContentProvider extends ContentProvider {

    // database
    private TigerTaskDBHelper database;



    private static final String AUTHORITY
        = "gov.multiversal.tasks4tigers2.provider";

    private static final String BASE_PATH
        = "tasks";

    public static final String CONTENT_TYPE
        = ContentResolver.CURSOR_DIR_BASE_TYPE + "/tasks";

    public static final String CONTENT_ITEM_TYPE
        = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/task";

    public static final String CONTENT_URI_PREFIX
        = "content://" + AUTHORITY + "/" + BASE_PATH + "/";


    public static final Uri CONTENT_URI
        = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);


    // URI Matcher
    private static final UriMatcher sURIMatcher = new UriMatcher( UriMatcher.NO_MATCH );
    private static final int TASKS = 1;
    private static final int TASK_ID = 2;   static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, TASKS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", TASK_ID);
    }

    @Override
    public boolean onCreate() {
        database = new TigerTaskDBHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // check if the caller has requested a column which does not exists
        TaskTable.validateProjection( projection );

        // Using SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables( TaskTable.TABLE_TASK );

        switch ( sURIMatcher.match(uri) ) {
            case TASKS:
                break;
            case TASK_ID:
                // add the task ID to the original query
                queryBuilder.appendWhere( TaskTable.COLUMN_ID + "=" + uri.getLastPathSegment() );
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
    public String getType( Uri uri ) {
        return null;
    }

    @Override
    public Uri insert( Uri uri, ContentValues values ) {

        SQLiteDatabase sqlDB = database.getWritableDatabase();

        long id = 0;
        switch ( sURIMatcher.match(uri) ) {
            case TASKS:
                id = sqlDB.insert(TaskTable.TABLE_TASK, null, values);
                break;
            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }

        getContext().getContentResolver().notifyChange( uri, null );

        return Uri.parse( CONTENT_URI_PREFIX + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        switch ( sURIMatcher.match(uri) ) {
            case TASKS:
                rowsDeleted = sqlDB.delete(TaskTable.TABLE_TASK, selection, selectionArgs);
                break;
            case TASK_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty( selection )) {
                    rowsDeleted = sqlDB.delete( TaskTable.TABLE_TASK,
                                                TaskTable.COLUMN_ID + "=" + id,
                                                null);
                } else {
                    rowsDeleted = sqlDB.delete( TaskTable.TABLE_TASK,
                                                TaskTable.COLUMN_ID + "=" + id
                                                    + " and " + selection,
                                                selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        switch ( sURIMatcher.match(uri) ) {
            case TASKS:
                rowsUpdated = sqlDB.update( TaskTable.TABLE_TASK,
                                            values,
                                            selection,
                                            selectionArgs);
                break;
            case TASK_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update( TaskTable.TABLE_TASK,
                                                values,
                                                TaskTable .COLUMN_ID + "=" + id,
                                                null );
                } else {
                    rowsUpdated = sqlDB.update( TaskTable.TABLE_TASK,
                                                values,
                                                TaskTable.COLUMN_ID + "=" + id
                                                    + " and " + selection,
                                                selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}