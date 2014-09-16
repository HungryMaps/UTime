/*
 * Autor: Ana Laura Berdasco
 * Clase ModificarCursos: Modifica el nombre del curso exietente
 */

package com.example.android.utime.app;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ModificarCurso extends Activity implements OnClickListener {

    EditText editartexto;
    Button btnActualizar, btnEliminar; //botones para actualizar y eliminar
    long curso_member_id;
    SQLControlador dbcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_curso);

        dbcon = new SQLControlador(this);
        dbcon.abrirBaseDeDatos();

        editartexto = (EditText) findViewById(R.id.et_curso_id);
        btnActualizar = (Button) findViewById(R.id.btnActualizar);
        btnEliminar = (Button) findViewById(R.id.btnEliminar);

        Intent intent = getIntent();
        String memberID = intent.getStringExtra("cursoId");
        String memberName = intent.getStringExtra("cursoNombre");

        curso_member_id = Long.parseLong(memberID);
        editartexto.setText(memberName);

        btnActualizar.setOnClickListener(this);
        btnEliminar.setOnClickListener(this);
    }

    /**
     * Despliega las opciones de actualizar o eliminar el curso
     * @param vista
     */
    @Override
    public void onClick(View vista) {
        // TODO Auto-generated method stub
        switch (vista.getId()) {
            case R.id.btnActualizar:
                String cursoName_upd = editartexto.getText().toString();
                dbcon.actualizarDatos(curso_member_id, cursoName_upd);
                this.returnHome();
                break;

            case R.id.btnEliminar:
                dbcon.deleteData(curso_member_id);
                this.returnHome();
                break;
        }
    }

    public void returnHome() {

        Intent home_intent = new Intent(getApplicationContext(),
                Cursos.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(home_intent);
    }
}
