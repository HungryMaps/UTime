package com.example.android.utime.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class CursoDetail extends ActionBarActivity implements android.view.View.OnClickListener {

    Button btnSave ,  btnDelete;
    Button btnClose;
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

        btnClose = (Button) findViewById(R.id.btnClose);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextProfesor = (EditText) findViewById(R.id.editTextProfesor);
        editTextAula = (EditText) findViewById(R.id.editTextAula);
        editTextDias = (EditText) findViewById(R.id.editTextDias);
        editTextHoras = (EditText) findViewById(R.id.editTextHoras);

        btnSave.setOnClickListener(this);

        btnClose.setOnClickListener(this);


        _Curso_Id =0;
        Intent intent = getIntent();
        _Curso_Id =intent.getIntExtra("student_Id", 0);
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

                Toast.makeText(this,"Nuevo Curso Insertado",Toast.LENGTH_SHORT).show();
            }
        }
        //falta update y delete
        else if (view== findViewById(R.id.btnClose)){
            finish();
        }


    }
}
