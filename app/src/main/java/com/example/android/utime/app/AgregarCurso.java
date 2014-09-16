/*
 * Autor: Jennifer Ledezma
 * Clase AgregarCursos: Agrega un curso nuevo a la lista de Cursos existentes
 */
package com.example.android.utime.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AgregarCurso extends Activity implements OnClickListener {
    EditText et;
    Button btnAgregar, read_bt;
    SQLControlador dbconeccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //Establece el campo para poder insertar/escribir un curso nuevo
        setContentView(R.layout.activity_agregar_curso);
        et = (EditText) findViewById(R.id.et_curso_id);
        //Implementa la accion de agregar el nuevo curso con el boton
        btnAgregar = (Button) findViewById(R.id.btnAgregarId);

        dbconeccion = new SQLControlador(this);
        dbconeccion.abrirBaseDeDatos();
        btnAgregar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnAgregarId:
                String name = et.getText().toString();
                //inserta los datos en la tabla de la base de datos
                dbconeccion.insertarDatos(name);
                Intent main = new Intent(AgregarCurso.this, Cursos.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(main);
                break;
            default:
                break;
        }
    }
}