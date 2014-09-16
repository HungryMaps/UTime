/*
 * Autor: Jennifer Ledezma
 * Clase Cursos: Muestra los cursos guardados en la app por el usuario, y opción de interactuar
 *               de distintas formas con estos (crear un nuevo curso, editar, eliminar)
 */

package com.example.android.utime.app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleCursorAdapter;

public class Cursos extends ActionBarActivity {

    Button btnAgregarCurso;
    ListView lista;
    SQLControlador dbconeccion;
    TextView textview_cursoID, textview_cursoNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cursos);

        dbconeccion = new SQLControlador(this);
        dbconeccion.abrirBaseDeDatos();
        btnAgregarCurso = (Button) findViewById(R.id.btnAgregarCurso);
        lista = (ListView) findViewById(R.id.listViewCursos);

        //acción del boton agregar miembro
        btnAgregarCurso.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAgregar = new Intent(Cursos.this, AgregarCurso.class);
                startActivity(intentAgregar);
            }
        });

        Cursor cursor = dbconeccion.leerDatos();

        String[] from = new String[] {
                DbHelper.CURSO_ID,
                DbHelper.CURSO_NOMBRE
        };
        int[] to = new int[] {
                R.id.curso_id,
                R.id.curso_nombre
        };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                Cursos.this, R.layout.formato_fila, cursor, from, to);

        adapter.notifyDataSetChanged();
        lista.setAdapter(adapter);

        // acción cuando hacemos click en item para poder modificarlo o eliminarlo
        lista.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {

                textview_cursoID = (TextView) view.findViewById(R.id.curso_id);
                textview_cursoNombre = (TextView) view.findViewById(R.id.curso_nombre);

                String aux_cursoId = textview_cursoID.getText().toString();
                String aux_cursoNombre = textview_cursoNombre.getText().toString();
            }
        });
    }
    //codigo viejo
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cursos, menu);
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
