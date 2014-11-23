package com.example.android.utime.app;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.sf.andpdf.pdfviewer.PdfViewerActivity;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * Autor: David Ramírez
 * Activity para hacer el manejo de archivos
 */

public class Archivos extends ListActivity {

    TextView archivo_id;
    File[] lista;
    String Curso_Name;
    /*
    * Se obtienen los archivos del almacenamiento externo y se filtran
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archivos);

        Intent intent = getIntent();
        Curso_Name = intent.getStringExtra("curso_name");

        FileFilter filtro = new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.getName().endsWith(".pdf")) {
                    return true;
                }
                return false;
            }
        };

        ListView listView = getListView();
        File sdDir = Environment.getExternalStorageDirectory();
        String path = sdDir.getPath()+"/Download";
        File descargasDir = new File(path);
        lista = descargasDir.listFiles(filtro);

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < lista.length; ++i) {
            list.add(lista[i].getName());
        }

        final StableArrayAdapter adapter = new StableArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
    }

   @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);
        String path = lista[(int)id].getAbsolutePath();
        openPdfIntent(path);
    }

    /*
    * Inicia el Intent para que se presenten los PDF
    * */
    private void openPdfIntent(String path)
    {
        try
        {
            final Intent intent = new Intent(Archivos.this, PDF.class);
            intent.putExtra(PdfViewerActivity.EXTRA_PDFFILENAME, path);
            startActivity(intent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
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

    // Adapter usado para agregar a la lista, hereda de Array Adapter

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
