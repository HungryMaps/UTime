/*
 * Proyecto de Ingeniría del Software II: UTime
 * Autores: Ana Laura Berdasco
 *          Jennifer Ledezma
 *          Paula López
 *          Joan Marchena
 *          David Ramírez
 * Clase que maneja el control de las funcionalidades e interacciones del Inicio
 * II Ciclo, 2014
 */

package com.example.android.utime.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;

public class MyMenu extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_menu);

        ImageButton botonCursos = (ImageButton)findViewById(R.id.cursos);
        ImageButton botonNotas = (ImageButton)findViewById(R.id.notas);
        ImageButton botonUbicacion = (ImageButton)findViewById(R.id.ubicaciones);
        ImageButton botonHorario = (ImageButton)findViewById(R.id.horario);
        ImageButton botonCalendario = (ImageButton)findViewById(R.id.caledario);
        ImageButton botonLinks = (ImageButton)findViewById(R.id.links);

        /**
         *Método que captura el boton al que se le ha dado click
         */
        botonLinks.setOnClickListener(new View.OnClickListener(){

            /**
             * REQ: que se haya capturado bien el botón al que se le hizo click
             * @param view
             * EFE: Carga en tiempo de ejecución una nueva vista o actividad según corresponda
             */
            @Override
            public void onClick(View view){
                //aquí le decimos de donde vamos (la ventana donde estoy) y hacia donde voy
                Intent in = new Intent(MyMenu.this, Links.class);
                //lanza la siguiente ventana
                startActivity(in);
            }
        });

        /**
         *Método que captura el boton al que se le ha dado click
         */
        botonCursos.setOnClickListener(new View.OnClickListener(){

            /**
             * REQ: que se haya capturado bien el botón al que se le hizo click
             * @param view
             * EFE: Carga en tiempo de ejecución una nueva vista o actividad según corresponda
             */
            @Override
            public void onClick(View view){
                //aquí le decimos de donde vamos (la ventana donde estoy) y hacia donde voy
                Intent in = new Intent(MyMenu.this, Cursos.class);
                //lanza la siguiente ventana
                startActivity(in);
            }
        });

        /**
         *Método que captura el boton al que se le ha dado click en este caso Fechas Importantes
         */
        botonNotas.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent in = new Intent(MyMenu.this, Notas.class);
                startActivity(in);
            }
        });

        /**
         *Método que captura el boton al que se le ha dado click en este caso Ubicaciones
         */
        botonUbicacion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //aquí le decimos de donde vamos (la ventana donde estoy) y hacia donde voy
                Intent in = new Intent(MyMenu.this, MapsActivity.class);
                //lanza la siguiente ventana
                startActivity(in);
            }
        });

        /**
         *Método que captura el boton al que se le ha dado click en este caso Horario
         */
        botonHorario.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //aquí le decimos de donde vamos (la ventana donde estoy) y hacia donde voy
                Intent in = new Intent(MyMenu.this, Horario.class);
                //lanza la siguiente ventana
                startActivity(in);
            }
        });

        /**
         *Método que captura el boton al que se le ha dado click
         */
        botonCalendario.setOnClickListener(new View.OnClickListener(){

            /**
             * REQ: que se haya capturado bien el botón al que se le hizo click
             * @param view
             * EFE: Carga en tiempo de ejecución una nueva vista o actividad según corresponda
             */
            @Override
            public void onClick(View view){
                //aquí le decimos de donde vamos (la ventana donde estoy) y hacia donde voy
                Intent in = new Intent(MyMenu.this, Calendario.class);
                //lanza la siguiente ventana
                startActivity(in);
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        insertarFecha();
        crearCarpetaArchivos();
    }

    /* Crea la carpeta que aloja todas las subcarpetas de cada curso en el almacenamiento externo */
    private void crearCarpetaArchivos(){
        if(isExternalStorageWritable()){
            File sdDir = Environment.getExternalStorageDirectory();
            String path = sdDir.getPath()+"/UTimeFiles";
            File file = new File(path);
            if(!file.exists()){
                file.mkdir();
            }
        }
    }

    /* Revisa que el almacenamiento externo esté disponible */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.menu_about:
                AlertDialog.Builder dialog = new AlertDialog.Builder(MyMenu.this);
                dialog.setTitle("About");
                dialog.setMessage("Universidad de Costa Rica\n" +
                                "Ingeniería del Software II\n\n" +
                                "Students: \n" +
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_my_menu, container, false);
            return rootView;
        }
    }

    public void insertarFecha() {
        ConnectFecha task = new ConnectFecha();
        task.execute();
    }

    /**
     * Método para revisar si hay conexión a internet
     * @return boolean
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
