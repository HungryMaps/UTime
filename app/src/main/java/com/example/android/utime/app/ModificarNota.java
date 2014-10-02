package com.example.android.utime.app;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ModificarNota extends Activity implements OnClickListener {

   /* EditText editartexto;
    Button btnActualizarNota, btnEliminarNota; //botones para actualizar y eliminar
    long nota_member_id;
    SQLControlador dbcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_nota);

        //Para conectarse con la base
        dbcon = new SQLControlador(this);
        dbcon.abrirBaseDeDatos();

        //Se guarda el texto que se va a modificar
        editartexto = (EditText) findViewById(R.id.nota_texto);
        btnActualizarNota = (Button) findViewById(R.id.btnActualizarNota);
        btnEliminarNota = (Button) findViewById(R.id.btnEliminarNota);

        Intent intent = getIntent();
        String memberID = intent.getStringExtra("notaId");
        String memberName = intent.getStringExtra("notaTexto");

        nota_member_id = Long.parseLong(memberID);
        editartexto.setText(memberName);

        //Para ponerle las acciones a los botones de Actualizar y Eliminar
        btnActualizarNota.setOnClickListener(this);
        btnEliminarNota.setOnClickListener(this);
    }

    /**
     * Despliega las opciones de actualizar o eliminar el curso
     * @param vista
     */


    @Override
    public void onClick(View vista) {
        /*// TODO Auto-generated method stub
        switch (vista.getId()) {
            case R.id.btnActualizarNota:
                String notaNombre_upd = editartexto.getText().toString();
                dbcon.actualizarDatosNotas(nota_member_id, notaNombre_upd);
                this.returnHome();
                break;

            case R.id.btnEliminarNota:
                dbcon.eliminarDataNotas(nota_member_id);
                this.returnHome();
                break;
        }
        */
    }

    /*
    *  Este método es para que, una vez que se ha modificado o eliminado una nota,
    *  Se sepa a dónde tiene que devolverse, en este caso a donde están todas las notas

    public void returnHome() {

        Intent home_intent = new Intent(getApplicationContext(),
                NotasGenerales.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }
     */
}
