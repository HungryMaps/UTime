package com.example.android.utime.app;

import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.utime.app.R;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

// Activity para hacer el manejo de archivos

public class Archivos extends ActionBarActivity {

    /*
    * Se obtienen los archivos del almacenamiento externo y se filtran
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archivos);

        FileFilter filtro = new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.getName().endsWith(".txt")) {
                    return true;
                }
                return false;
            }
        };

        File sdDir = Environment.getExternalStorageDirectory();
        String path = sdDir.getPath()+"/Download";
        File descargasDir = new File(path);
        File[] lista = descargasDir.listFiles(filtro);
        for(int i=0; i<lista.length; i++){
            Toast.makeText(this, lista[i].getName(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.archivos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
