package com.example.android.utime.app;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class AgregarNota extends Activity implements OnClickListener {

    EditText et;
    Button btnAgregarId, read_bt;
    SQLControlador dbconeccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Espacio para poder escribir una nota nueva
        setContentView(R.layout.activity_agregar_nota);
        et = (EditText) findViewById(R.id.nota_texto);

        //Acción del botón para agregar una nota nueva
        btnAgregarId = (Button) findViewById(R.id.btnAgregarId);

        dbconeccion = new SQLControlador(this);
        dbconeccion.abrirBaseDeDatos();
        btnAgregarId.setOnClickListener(this);

    }//onCreate

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnAgregarId:
                String name = et.getText().toString();
                //inserta los datos en la tabla de la base de datos
                dbconeccion.insertarDatosNotas(name);
                Intent main = new Intent(AgregarNota.this, Horario.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(main);
                break;
            default:
                break;
        }
    }


}
