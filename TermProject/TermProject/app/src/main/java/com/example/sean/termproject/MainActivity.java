package com.example.sean.termproject;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.sean.termproject.Fragments.HueFragment;
import com.example.sean.termproject.Fragments.ResultFragment;
import com.example.sean.termproject.Fragments.SaturationFragment;
import com.example.sean.termproject.Fragments.ValueFragment;
import com.example.sean.termproject.Interfaces.FragmentCallback;
import com.example.sean.termproject.SwatchHelper.SwatchHelper;


public class MainActivity extends FragmentActivity implements FragmentCallback{

    FragmentManager fm;
    SwatchHelper mSavedSwatch;
    private int explorerFragmentKey = 0;

    private static int EXPLORER_HUE = 0;
    private static int EXPLORER_SATURATION = 1;
    private static int EXPLORER_VALUE = 2;
    private static int EXPLORER_RESULT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm = getFragmentManager();
        if( findViewById( R.id.fragment_container ) != null ) {
            if( savedInstanceState == null ) {
                startExplorer();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
/*
    failed attempt at back button functionality
    Caused more harm than good
    @Override
    public void onBackPressed() {
        switch ( explorerFragmentKey ) {
            case 1: // EXPLORER_SATURATION
                startExplorer();
                break;
            case 2: // EXPLORER_VALUE
                selectHue( mSavedSwatch.diffSat( 1.0f ) );
                break;
            case 3: // EXPLORER_RESULT
                selectSaturation( mSavedSwatch.diffVal( 1.0f ) );
                break;
            default:
                super.onBackPressed();
                break;
        }
    }
    */

    @Override
    public void startExplorer() {
        mSavedSwatch = null;

        explorerFragmentKey = EXPLORER_HUE;

        fm.beginTransaction()
                .add( R.id.fragment_container,
                        new HueFragment() )
                .addToBackStack(null)
                .commit();

        Toast.makeText(
                this,
                "Select a Hue!",
                Toast.LENGTH_SHORT)
                .show();
    }
    @Override
    public void selectHue(SwatchHelper swatch) {
        mSavedSwatch = swatch;

        explorerFragmentKey = EXPLORER_SATURATION;

        fm.beginTransaction()
                .replace( R.id.fragment_container,
                          new SaturationFragment())
                .addToBackStack( null )
                .commit();

        Toast.makeText(
                this,
                "Hue Selected!\nNow, select the Saturation",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void selectSaturation(SwatchHelper swatch) {
        mSavedSwatch = swatch;

        explorerFragmentKey = EXPLORER_VALUE;

        fm.beginTransaction()
                .replace( R.id.fragment_container,
                          new ValueFragment() )
                .addToBackStack( null )
                .commit();

        Toast.makeText(
                this,
                "Saturation Selected!\nNow, select the Value",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void selectValue(SwatchHelper swatch) {
        mSavedSwatch = swatch;

        explorerFragmentKey = EXPLORER_RESULT;

        fm.beginTransaction()
                .replace( R.id.fragment_container,
                          new ResultFragment() )
                .addToBackStack(null)
                .commit();

        Toast.makeText(
                this,
                "Value Selected!\nHere is the result",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public SwatchHelper getSwatch() {
        return mSavedSwatch;
    }

    @Override
    public void restartExplorer() {
        mSavedSwatch = null;
        startActivity( new Intent( this, MainActivity.class ) );
    }
}
