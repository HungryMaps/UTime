/*
 * Autores: Jennifer Ledezma
 *          Ana Laura Berdasco
 * Clase NotaDetail: Mantiene la conexión entre las funciones propias de sql y la interfaz
 */

package com.example.android.utime.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NotaDetail extends ActionBarActivity implements android.view.View.OnClickListener {

    Button btnSave;
    Button btnDelete;
    EditText editTextNameNota;
    EditText editTextComentarioNota;
    private int _Nota_Id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota_detail);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        editTextNameNota = (EditText) findViewById(R.id.editTextNameNota);
        editTextComentarioNota = (EditText) findViewById(R.id.editTextComentarioNota);

        btnSave.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        _Nota_Id = 0;
        Intent intent = getIntent();
        _Nota_Id = intent.getIntExtra("nota_Id", 0);
        SQLControlador repo = new SQLControlador(this);
        Nota nota = new Nota();
        nota = repo.getNotaById(_Nota_Id);

        editTextNameNota.setText(nota.nameNota);
        editTextComentarioNota.setText(nota.comentarioNota);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nota_detail, menu);
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

    public void onClick(View view) {
        if (view == findViewById(R.id.btnSave)) {
            SQLControlador repo = new SQLControlador(this);
            Nota nota = new Nota();

            nota.comentarioNota = editTextComentarioNota.getText().toString();
            nota.nameNota = editTextNameNota.getText().toString();
            nota.nota_ID = _Nota_Id;

            if (_Nota_Id == 0) {
                _Nota_Id = repo.insertNota(nota);

                Toast.makeText(this, "Nueva Nota Insertada", Toast.LENGTH_SHORT).show();
            }
                else{
                    repo.updateNota(nota);
                    Toast.makeText(this,"Curso Actualizado",Toast.LENGTH_SHORT).show();
                }
            //aqui actualizar
           finish();
        }

        else if (view== findViewById(R.id.btnDelete)){
            SQLControlador erase = new SQLControlador(this);
            erase.deleteNota(_Nota_Id);
            Toast.makeText(this, "Nota Eliminada", Toast.LENGTH_SHORT);

            finish(); // para que vuelva a la pagina de cursos
        }
    }
}