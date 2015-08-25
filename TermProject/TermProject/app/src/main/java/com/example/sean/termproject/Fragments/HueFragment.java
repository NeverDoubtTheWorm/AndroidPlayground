package com.example.sean.termproject.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sean.termproject.Adapter.ColorAdapter;
import com.example.sean.termproject.Interfaces.FragmentCallback;
import com.example.sean.termproject.R;
import com.example.sean.termproject.SwatchHelper.SwatchHelper;

import java.util.ArrayList;

/**
 * Created by Sean Thomas on 4/22/2015.
 */
public class HueFragment extends ListFragment{

    private ArrayList<SwatchHelper> mColorList = null;
    private FragmentCallback mCallbacks;
    private ColorAdapter mAdapter = null;

    private static int MAX_SWATCH_COUNT = 256;
    private static int MAX_CENTER_HUE = 360;
    private int tempSwatchCount;
    private int tempHueCenter;
    private int mSwatchCount;
    private static final String HUE_SWATCH_COUNT_PREF = "HueSwatchCount";
    private float mHueCenter;
    private static final String HUE_CENTER_PREF = "HueCenter";

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
        View v = inflater.inflate(R.layout.fragment_explorer, container, false);

        return v;
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );

        mSwatchCount = getActivity().getPreferences( Context.MODE_PRIVATE )
                .getInt( HUE_SWATCH_COUNT_PREF, 8 );
        mHueCenter = getActivity().getPreferences( Context.MODE_PRIVATE )
                .getFloat( HUE_CENTER_PREF, 0.0f );
        mColorList = SwatchHelper.createHueSwatches( mSwatchCount, mHueCenter );
        mAdapter = new ColorAdapter( getActivity(), mColorList );

        tempHueCenter = (int) mHueCenter;
        tempSwatchCount = mSwatchCount;

        setListAdapter( mAdapter );

        ( (Button) getActivity().findViewById( R.id.swatchConfigButton ) )
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                final TextView swatchTextView = new TextView( getActivity() );
                                swatchTextView.setText(String.format("Swatch Count: %d", mSwatchCount));
                                swatchTextView.setGravity( View.TEXT_ALIGNMENT_CENTER );

                                SeekBar swatchSeekbar = new SeekBar( getActivity() );
                                swatchSeekbar.setMax( MAX_SWATCH_COUNT - 1 );
                                swatchSeekbar.setProgress( mSwatchCount );
                                swatchSeekbar.setOnSeekBarChangeListener(
                                        new SeekBar.OnSeekBarChangeListener() {
                                            @Override
                                            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                                tempSwatchCount = seekBar.getProgress() + 1;
                                                swatchTextView.setText( String.format("Swatch Count: %d", tempSwatchCount) );
                                            }

                                            @Override
                                            public void onStartTrackingTouch(SeekBar seekBar) {

                                            }

                                            @Override
                                            public void onStopTrackingTouch(SeekBar seekBar) {

                                            }
                                        }
                                );

                                final TextView hueTextView = new TextView( getActivity() );
                                hueTextView.setText( String.format("Center Hue: %d", (int) mHueCenter) );
                                hueTextView.setGravity(View.TEXT_ALIGNMENT_CENTER);

                                SeekBar hueSeekBar = new SeekBar( getActivity() );
                                hueSeekBar.setMax( MAX_CENTER_HUE );
                                hueSeekBar.setProgress( (int) mHueCenter );
                                hueSeekBar.setBackgroundColor(
                                        SwatchHelper.HSVtoColor(
                                                mHueCenter,
                                                1.0f,
                                                1.0f
                                        )
                                );
                                hueSeekBar.setOnSeekBarChangeListener(
                                        new SeekBar.OnSeekBarChangeListener() {
                                            @Override
                                            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                                tempHueCenter = seekBar.getProgress();
                                                hueTextView.setText(String.format("Center Hue: %d", tempHueCenter));
                                                seekBar.setBackgroundColor(
                                                        SwatchHelper.HSVtoColor(
                                                                (float) tempHueCenter,
                                                                1.0f,
                                                                1.0f
                                                        )
                                                );
                                            }

                                            @Override
                                            public void onStartTrackingTouch(SeekBar seekBar) {

                                            }

                                            @Override
                                            public void onStopTrackingTouch(SeekBar seekBar) {

                                            }
                                        }
                                );

                                LinearLayout layout = new LinearLayout( getActivity() );
                                layout.setOrientation( LinearLayout.VERTICAL );
                                layout.setPadding(10, 10, 10, 10);
                                layout.addView(swatchTextView);
                                layout.addView(swatchSeekbar);
                                layout.addView( hueTextView );
                                layout.addView( hueSeekBar );


                                new AlertDialog.Builder( getActivity() )
                                        .setTitle("Choose the number of swatches and the center hue")
                                        .setView(layout)
                                        .setPositiveButton(android.R.string.yes,
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int option) {
                                                        mSwatchCount = tempSwatchCount;
                                                        mHueCenter = (float) tempHueCenter;
                                                        Toast.makeText(
                                                                getActivity(),
                                                                String.format( "Using %d swatches.\n Center Hue is %d", mSwatchCount, (int) mHueCenter),
                                                                Toast.LENGTH_SHORT)
                                                                .show();
                                                        savePrefs();
                                                        reSwatch();
                                                    }
                                                })
                                        .setNegativeButton(android.R.string.cancel,
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int option) {
                                                        // Do Nothing
                                                    }
                                                })
                                        .show();
                            }
                        }
                );
    }

    private void reSwatch() {
        mColorList = SwatchHelper.createHueSwatches(mSwatchCount, mHueCenter);
        mAdapter = new ColorAdapter( getActivity(), mColorList );

        setListAdapter( mAdapter );
    }

    @Override
    public void onListItemClick( ListView listView,
                                 View view,
                                 int position,
                                 long id ) {
        super.onListItemClick( listView, view, position, id );
        mCallbacks.selectHue( mAdapter.getSwatch( position ) );
    }

    @Override
    public void onPause() {
        super.onPause();

        savePrefs();
    }

    private void savePrefs(){
        SharedPreferences.Editor editor = getActivity()
                .getPreferences(Context.MODE_PRIVATE).edit();
        editor.putInt( HUE_SWATCH_COUNT_PREF, mSwatchCount );
        editor.putFloat( HUE_CENTER_PREF, mHueCenter );
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
}
