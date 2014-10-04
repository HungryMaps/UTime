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

public class CursoDetail extends ActionBarActivity implements android.view.View.OnClickListener {

    Button btnSave ,  btnDelete;

    EditText editTextName;
    EditText editTextProfesor;
    EditText editTextAula;
    EditText editTextDias;
    EditText editTextHoras;
    private int _Curso_Id=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curso_detail);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);


        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextProfesor = (EditText) findViewById(R.id.editTextProfesor);
        editTextAula = (EditText) findViewById(R.id.editTextAula);
        editTextDias = (EditText) findViewById(R.id.editTextDias);
        editTextHoras = (EditText) findViewById(R.id.editTextHoras);

        btnSave.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
       ;

        _Curso_Id =0;
        Intent intent = getIntent();
        _Curso_Id =intent.getIntExtra("curso_Id", 0);
        SQLControlador repo = new SQLControlador(this);
        Curso curso = new Curso();
        curso = repo.getCursoById(_Curso_Id);

        editTextAula.setText(curso.aula);
        editTextName.setText(curso.name);
        editTextProfesor.setText(curso.profesor);
        editTextDias.setText(curso.dias);
        editTextHoras.setText(curso.horas);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.curso_detail, menu);
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
        if (view == findViewById(R.id.btnSave)){
            SQLControlador repo = new SQLControlador(this);
            Curso curso = new Curso();
            curso.horas = editTextHoras.getText().toString();
            curso.dias = editTextDias.getText().toString();
            curso.aula= editTextAula.getText().toString();
            curso.profesor=editTextProfesor.getText().toString();
            curso.name=editTextName.getText().toString();
            curso.curso_ID=_Curso_Id;

            if (_Curso_Id==0){
                _Curso_Id = repo.insert(curso);

                Toast.makeText(this,"Se agrego un nuevo curso",Toast.LENGTH_SHORT).show();
            }
            else{

                repo.update(curso);
                Toast.makeText(this,"Curso Actualizado",Toast.LENGTH_SHORT).show();
            }

           finish();

        }

        else if (view== findViewById(R.id.btnDelete)){
            SQLControlador erase = new SQLControlador(this);
            erase.delete(_Curso_Id);
            Toast.makeText(this, "Curso Eliminado", Toast.LENGTH_SHORT);

            finish();
        }

    }

    public void returnHome() {

        Intent home_intent = new Intent(getApplicationContext(),
                Cursos.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(home_intent);

    }
}
