package com.example.android.utime.app;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

/**
 * Created by Joan Marchena on 06/09/14.
 */
public class Calendario extends ActionBarActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendario);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }
}
