package com.example.android.utime.app;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/* Clase para Ver la carpeta de Download dentro de la SD externa y seleccionar los archivos */
public class VisualizadorSD extends ListActivity {

    TextView archivo_id;
    File[] lista;
    String Curso_Name;
    /*
    * Se obtienen los archivos del almacenamiento externo y se filtran
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizador_sd);

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
        Intent intent = getIntent();
        String direccion = intent.getStringExtra("direccion")+"/"+lista[(int)id].getName();
        File fuente = new File(path);
        File destino = new File(direccion);
        try {
            copiarArchivo(fuente, destino);
            Toast.makeText(this, "Se agregó el siguiente archivo exitosamente: "+lista[(int)id].getName(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.visualizador_sd, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            //Caso que el usuario escoge about del Menu: Muestra información de la app
            case R.id.menu_about:

                AlertDialog.Builder dialog = new AlertDialog.Builder(VisualizadorSD.this);
                dialog.setTitle("About");
                dialog.setMessage("Universidad de Costa Rica\n" +
                                "Ingeniería del Software II\n\n" +
                                "Students: \n"+
                                "Ana Laura Berdasco, " +
                                "Jennifer Ledezma, " +
                                "Paula Lopez, " +
                                "Joan Marchena, " +
                                "David Ramirez\n\n" +
                                "UTime\n\n"
                                + "If there is any bug is found please freely e-mail us: " +
                                "\n\tutime@gmail.com"
                );
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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


    /* Método para copiar el archivos a la carpeta destino (es decir la del curso anterior) */
    private static void copiarArchivo(File source, File dest) throws IOException {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destChannel = new FileOutputStream(dest).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            sourceChannel.close();
            destChannel.close();
        }
    }
}
