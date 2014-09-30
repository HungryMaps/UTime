/*
 * Autor: Jennifer Ledezma
 * Clase Horario: Muestra el horario guardado en la app por el usuario, y opción de interactuar
 *               de distintas formas con este (crear un nuevo curso, editar, eliminar)
 */

package com.example.android.utime.app;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class Horario extends ActionBarActivity {

    Button btnAgregarNota;
    ListView lista;
    SQLControlador dbconeccion;
    TextView textView_notaID, textView_notaTexto;


    /*
    * Se le agrega más funcionalidad al método onCreate para que aparesca la tabla de notas generales
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);

        dbconeccion = new SQLControlador(this);
        dbconeccion.abrirBaseDeDatos();
        btnAgregarNota = (Button) findViewById(R.id.btnAgregarNota);
        lista = (ListView) findViewById(R.id.listViewNotas);

        /**
         * Acción del botón agregar nota.
         */
        btnAgregarNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAgregar = new Intent(Horario.this, AgregarNota.class);
                startActivity(intentAgregar);
            }
        });

        // Traer los datos de la base, se ponen en el cursor para después ponerlos en el adapter.
        Cursor cursor = dbconeccion.leerDatosNotas();

        String[] from = new String[] {
                DBhelper.NOTA_ID,
                DBhelper.NOTA_TEXTO
        };

        int[] to = new int[] {
                R.id.nota_id,
                R.id.nota_texto
        };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                Horario.this, R.layout.formato_nota, cursor, from, to);

        adapter.notifyDataSetChanged();
        lista.setAdapter(adapter);

        // Para cuando se le da click para verlo y poder modificar o eliminar la nota
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {

                textView_notaID = (TextView) view.findViewById(R.id.nota_id);
                textView_notaTexto = (TextView) view.findViewById(R.id.nota_texto);

                String notaId = textView_notaID.getText().toString();
                String notaTexto = textView_notaTexto.getText().toString();

                Intent modify_intent = new Intent(getApplicationContext(), ModificarNota.class);
                modify_intent.putExtra("notaId", notaId);
                modify_intent.putExtra("notaTexto", notaTexto);
                startActivity(modify_intent);

            }
        });

    } //onCreate


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.horario, menu);
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
