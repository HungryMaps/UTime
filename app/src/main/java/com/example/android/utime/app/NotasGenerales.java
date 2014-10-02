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


public class NotasGenerales extends ActionBarActivity {

    Button btnAgregarNota;
    ListView lista;
    SQLControlador dbconeccion;
    TextView textView_notaID, textView_notaTexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas_generales);

        /*
        En este caso, se accesa a la versión 2 de la base de datos
         */
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
                Intent intentAgregar = new Intent(NotasGenerales.this, AgregarNota.class);
                startActivity(intentAgregar);
            }
        });

        // Traer los datos de la base, se ponen en el cursor para después ponerlos en el adapter.
        Cursor cursor = dbconeccion.leerDatosNotas();

        //Aquí se van a poner los nombres de las columnas de la tabla de Notas
        String[] from = new String[] {
                DBhelper.NOTA_ID,
                DBhelper.NOTA_TEXTO
        };

        //Aquí se ponen los datos que se van a guardar en la tabla de  Notas
        int[] to = new int[] {
                R.id.nota_id,
                R.id.nota_texto
        };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                NotasGenerales.this, R.layout.formato_nota, cursor, from, to);

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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.notas_generales, menu);
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
