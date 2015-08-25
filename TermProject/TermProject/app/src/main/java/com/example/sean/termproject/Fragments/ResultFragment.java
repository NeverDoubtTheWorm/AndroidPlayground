package com.example.sean.termproject.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sean.termproject.Adapter.ColorCursorAdapter;
import com.example.sean.termproject.Interfaces.FragmentCallback;
import com.example.sean.termproject.R;
import com.example.sean.termproject.SwatchHelper.SwatchHelper;
import com.example.sean.termproject.db.ColorContentProvider;
import com.example.sean.termproject.db.ColorTable;

/**
 * Created by Sean Thomas on 4/23/2015.
 */
public class ResultFragment
        extends ListFragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private FragmentCallback mCallbacks;
    private ColorCursorAdapter mAdapter = null;
    private SwatchHelper mSwatch;


    private String mLeftHue;
    private String mRightHue;
    private String mLeftSat;
    private String mRightSat;
    private String mLeftVal;
    private String mRightVal;

    private String[] SortOptions = { "Name",
                                     "Hue, Saturation, Value",
                                     "Hue, Value, Saturation",
                                     "Saturation, Hue, Value",
                                     "Saturation, Value, Hue",
                                     "Value, Hue, Saturation",
                                     "Value, Saturation, Hue",
                                   };
    private static String SORT_PREF = "mSPSortPreference";
    private int mSortPreference;
    private int tempSelect;

    @Override
    public void onAttach(Activity activity) {
//        if (DEBUG) Log.i(TAG, "onAttach(Activity)");
        super.onAttach(activity);
        if (!(activity instanceof FragmentCallback)) {
            throw new IllegalStateException("Activity must implement the TaskCallbacks interface.");
        }

        // Hold a reference to the parent Activity so we can report back the task's
        // current progress and results.
        mCallbacks = (FragmentCallback) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    /**
     * This method is called once when the Fragment is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //       if (DEBUG) Log.i(TAG, "onCreate(Bundle)");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_result, container, false);
        mSwatch = mCallbacks.getSwatch();
        double satRange = 1.0 / getActivity().getPreferences( Context.MODE_PRIVATE )
                .getInt( SaturationFragment.SAT_SWATCH_COUNT_PREF, 8 );
        double valRange = 1.0 / getActivity().getPreferences( Context.MODE_PRIVATE )
                .getInt( ValueFragment.VAL_SWATCH_COUNT_PREF, 8 );
        mLeftHue  = Integer.toString( (int)
                mSwatch.getLHue() );
        mRightHue = Integer.toString( (int)
                mSwatch.getRHue() );
        mLeftSat  = Integer.toString( (int)
                ( ( mSwatch.getSaturation() - satRange ) * 100 ) % 100 );
        mRightSat = Integer.toString( (int)
                ( mSwatch.getSaturation() * 100 ) ) ;
        mLeftVal  = Integer.toString( (int)
                ( ( mSwatch.getValue() - valRange ) * 100 )  % 100 );
        mRightVal = Integer.toString( (int)
                ( mSwatch.getValue() * 100 ) );


        TextView mPreviewSwatch = (TextView) view.findViewById( R.id.picked_swatch );
        mPreviewSwatch.setBackground( mSwatch.getGradient() );

        TextView mHueRangeTextView = (TextView) view.findViewById( R.id.hue_range );
        mHueRangeTextView.setText(
                String.format( "Hue : %s ~ %s",
                        mLeftHue,
                        mRightHue
                ) );
        TextView mSatRangeTextView = (TextView) view.findViewById( R.id.sat_range );
        mSatRangeTextView.setText(
                String.format( "Saturation: %s ~ %s",
                        mLeftSat,
                        mRightSat
                ) );
        TextView mValRangeTextView = (TextView) view.findViewById( R.id.val_range );
        mValRangeTextView.setText(
                String.format( "Value: %s ~ %s",
                        mLeftVal,
                        mRightVal
                ) );

        mSortPreference = getActivity().getPreferences( Context.MODE_PRIVATE )
                .getInt( SORT_PREF, 0 );
        Button sortButton = (Button) view.findViewById( R.id.sortButton );
        sortButton.setOnClickListener( ( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder( getActivity() )
                    .setTitle("Sort by")
                    .setSingleChoiceItems( SortOptions,
                                           mSortPreference,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int option) {
                                tempSelect = option;
                            } } )
                    .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int option) {
                                mSortPreference = tempSelect;
                                Toast.makeText(
                                        getActivity(),
                                        "Sorting by " + SortOptions[mSortPreference],
                                        Toast.LENGTH_SHORT)
                                        .show();
                                savePrefs();
                                reSort();
                            }
                        } )
                    .setNegativeButton( android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int option) {
                                // Do Nothing
                            }
                        } )
                    .show();
        } } ) );

        ( (Button) view.findViewById( R.id.restartButton ) )
            .setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCallbacks.restartExplorer();
                    }
                } );

        return view;
    }

    private void reSort() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );

        fillData();

        setListAdapter( mAdapter );

    }

    private void fillData() {
        getLoaderManager().initLoader( 0, null, this );

        mAdapter = new ColorCursorAdapter( getActivity(), null, 0 );
    }

    @Override
    public void onListItemClick( ListView listView,
                                 View view,
                                 int position,
                                 long id ) {
        super.onListItemClick( listView, view, position, id );

        Cursor c = mAdapter.getCursor();
        c.moveToPosition(position);
        Toast.makeText(
                getActivity(),
                String.format(
                        "%s \nHue: %d \nSaturation: %d \nValue: %d",
                        c.getString( ColorCursorAdapter.NAME ),
                        c.getInt( ColorCursorAdapter.HUE ),
                        c.getInt( ColorCursorAdapter.SATURATION ),
                        c.getInt( ColorCursorAdapter.VALUE )
                    ),
                Toast.LENGTH_SHORT
                ).show();
    }

    @Override
    public void onPause() {
        super.onPause();

        savePrefs();
    }

    private void savePrefs() {
        SharedPreferences.Editor editor = getActivity()
                .getPreferences(Context.MODE_PRIVATE).edit();
        editor.putInt( SORT_PREF, mSortPreference );
        editor.apply();
    }
    /**
     * Note that this method is <em>not</em> called when the Fragment is being
     * retained across Activity instances. It will, however, be called when its
     * parent Activity is being destroyed for good (such as when the user clicks
     * the back button, etc.).
     */
    @Override
    public void onDestroy() {
        //       if (DEBUG) Log.i(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String SEARCH_IN_RANGE ="( %s >=? AND %s <=? )";
        String findIn;
        String[] args;
        if ( mSwatch.getLHue() < mSwatch.getRHue() ) {
            findIn = String.format(
                       SEARCH_IN_RANGE
                     + " AND "
                     + SEARCH_IN_RANGE
                     + " AND "
                     + SEARCH_IN_RANGE,
                    ColorTable.COLUMN_HUE,          ColorTable.COLUMN_HUE,
                    ColorTable.COLUMN_SATURATION,   ColorTable.COLUMN_SATURATION,
                    ColorTable.COLUMN_VALUE,        ColorTable.COLUMN_VALUE
            );
            args = new String[] {
                    mLeftHue, mRightHue,
                    mLeftSat, mRightSat,
                    mLeftVal, mRightVal
            };
        } else if ( mSwatch.getLHue() > mSwatch.getRHue() ) {
            findIn = String.format(
                       SEARCH_IN_RANGE
                     + " AND "
                     + SEARCH_IN_RANGE
                     + " AND "
                     + SEARCH_IN_RANGE
                     + " AND "
                     + SEARCH_IN_RANGE,
                    ColorTable.COLUMN_HUE,          ColorTable.COLUMN_HUE,
                    ColorTable.COLUMN_HUE,          ColorTable.COLUMN_HUE,
                    ColorTable.COLUMN_SATURATION,   ColorTable.COLUMN_SATURATION,
                    ColorTable.COLUMN_VALUE,        ColorTable.COLUMN_VALUE
            );
            args = new String[] {
                    mLeftHue,   "360",
                    "0",        mRightHue,
                    mLeftSat,   mRightSat,
                    mLeftVal,   mRightVal
            };
        } else {
            findIn = String.format(
                      SEARCH_IN_RANGE
                    + " AND "
                    + SEARCH_IN_RANGE,
                    ColorTable.COLUMN_SATURATION,   ColorTable.COLUMN_SATURATION,
                    ColorTable.COLUMN_VALUE,        ColorTable.COLUMN_VALUE
            );
            args = new String[] {
                    mLeftSat, mRightSat,
                    mLeftVal, mRightVal
            };
        }

        return new CursorLoader(
                getActivity(),
                ColorContentProvider.CONTENT_URI,
                ColorCursorAdapter.PROJECTION,
                findIn,
                args,
                SortOptions[mSortPreference] );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mAdapter.swapCursor( cursor );
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor( null );
    }
}
