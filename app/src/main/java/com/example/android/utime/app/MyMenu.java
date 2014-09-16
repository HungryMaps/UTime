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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MyMenu extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_menu);
        Button botonCursos = (Button)findViewById(R.id.cursos);
        Button botonFechImp = (Button)findViewById(R.id.fechas_importantes);
        Button botonUbicacion = (Button)findViewById(R.id.ubicaciones);
        Button botonHorario = (Button)findViewById(R.id.horario);
        Button botonCalendario = (Button)findViewById(R.id.caledario);

        /**
         *Método que captura el boton al que se le ha dado click en este caso Fechas Importantes
         */
        botonCursos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent in = new Intent(MyMenu.this, Cursos.class);
                startActivity(in);
            }
        });

        /**
         *Método que captura el boton al que se le ha dado click en este caso Fechas Importantes
         */
        botonFechImp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent in = new Intent(MyMenu.this, FechasImportantes.class);
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
                Intent in = new Intent(MyMenu.this, Ubicaciones.class);
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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
}
