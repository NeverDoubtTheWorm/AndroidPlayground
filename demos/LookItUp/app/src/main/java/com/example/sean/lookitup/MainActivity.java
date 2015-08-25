package com.example.sean.lookitup;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    private DictionaryDAO mDB;
    private EditText mTerm;
    private EditText mDefinition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDB = new DictionaryDAO(this.getApplicationContext());
        mTerm = (EditText) findViewById(R.id.termEditText);
        mDefinition = (EditText) findViewById(R.id.DefEditText);
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

    public void onLookUpButtonClick(View v){
        Cursor mCursor = mDB.fetchAllEntries();
        mCursor.moveToFirst();
        String term = mTerm.getText().toString();
        String input = "";
        String def = "";
        boolean foundIt = false;
        while( !mCursor.isAfterLast() ){
            input = mCursor.getString( 0 );
            def = mCursor.getString( 1 );
            Toast.makeText(getApplicationContext(), "TERM: " + input + " DEF: " + def, Toast.LENGTH_SHORT).show();
            if( input.equals(term) ){
                foundIt = true;
                def = mCursor.getString( 1 );
                break;
            }
            mCursor.moveToNext();
        }
        mCursor.close();

        if( foundIt ){
            mTerm.setText( term );
            mDefinition.setText( def );
            Toast.makeText(getApplicationContext(), "Found " + term, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), term + " Not Found", Toast.LENGTH_SHORT).show();
        }
    }

    public void onUpdateButtonClick(View v){
        Cursor mCursor = mDB.fetchAllEntries();
        mCursor.moveToFirst();
        String term = mTerm.getText().toString();
        String input = "";
        String def = "";
        boolean foundIt = false;
        while( !mCursor.isAfterLast() ){
            input = mCursor.getString( 0 );
            if( input.equals(term) ){
                foundIt = true;
                break;
            }
            mCursor.moveToNext();
        }
        mCursor.close();

        if( foundIt ){
            mDB.updateDictionaryEntry( term, def );
            Toast.makeText(getApplicationContext(), term + " Updated", Toast.LENGTH_SHORT).show();
        } else {
            mDB.createDictionaryEntry( term, def );
            Toast.makeText(getApplicationContext(), term + " Created", Toast.LENGTH_SHORT).show();
        }
    }

    public void onRemoveButtonClick(View v){
        Cursor mCursor = mDB.fetchAllEntries();
        mCursor.moveToFirst();
        String term = mTerm.getText().toString();
        String input = "";
        boolean foundIt = false;
        while( !mCursor.isAfterLast() ){
            input = mCursor.getString( 0 );
            if( input.equals( term ) ){
                foundIt = true;
                break;
            }
            mCursor.moveToNext();
        }
        mCursor.close();

        if( foundIt ){
            mDB.deleteDictionaryEntry( term );
            Toast.makeText(getApplicationContext(), "Found " + term, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), term + " Not Found", Toast.LENGTH_SHORT).show();
        }
    }
}
