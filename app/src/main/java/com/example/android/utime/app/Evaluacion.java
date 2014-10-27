package com.example.android.utime.app;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;


public class Evaluacion extends ActionBarActivity implements android.view.View.OnClickListener{

    private int Curso_Id=0;
    Button btnSave;
    int contador = 2; // Para uso futuro
    List<Integer> evaluaciones = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluacion);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        Intent intent = getIntent();
        Curso_Id = intent.getIntExtra("curso_ID", 0);

        // Aquí se consulta con un Where respecto al Curso cuyas evaluaciones tiene asociadas

        DBhelper dbhelper = new DBhelper(getApplicationContext());
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String [] columnas =  {
            EvaluacionPorCurso.KEY_ID,
            EvaluacionPorCurso.KEY_Calificacion,
            EvaluacionPorCurso.KEY_Evaluacion,
            EvaluacionPorCurso.KEY_name,
            EvaluacionPorCurso.KEY_ID_Curso
        };
        String [] parametros = {
            ""+Curso_Id
        };
        Cursor cursor = db.query(EvaluacionPorCurso.TABLE, columnas, EvaluacionPorCurso.KEY_ID_Curso+" = ?", parametros, null, null, null);

        if (cursor.moveToFirst()) {
            int i=2;

            // Se presentan las evaluaciones en el layout
            do {

                evaluaciones.add(Integer.parseInt(cursor.getString(cursor.getColumnIndex(EvaluacionPorCurso.KEY_ID))));

                String celda = "editText"+Integer.toString(i);
                int id = getResources().getIdentifier(celda, "id", getPackageName());
                EditText currcell = (EditText) findViewById(id);
                currcell.setText(cursor.getString(cursor.getColumnIndex(EvaluacionPorCurso.KEY_name)));

                celda = "editText"+Integer.toString(i+1);
                id = getResources().getIdentifier(celda, "id", getPackageName());
                currcell = (EditText) findViewById(id);
                currcell.setText(cursor.getString(cursor.getColumnIndex(EvaluacionPorCurso.KEY_Evaluacion)));

                celda = "editText"+Integer.toString(i+2);
                id = getResources().getIdentifier(celda, "id", getPackageName());
                currcell = (EditText) findViewById(id);
                currcell.setText(cursor.getString(cursor.getColumnIndex(EvaluacionPorCurso.KEY_Calificacion)));

                i+=3;
            } while (cursor.moveToNext());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.evaluacion, menu);
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

    // Para cuando se apreta el botón de salvar
    @Override
    public void onClick(View view) {
        int k=0;
        for(int i=2; i<29; i+=3) {
            String[] dato = new String[3];

            // Saca datos del layout
            String celda = "editText" + Integer.toString(i);
            int id = getResources().getIdentifier(celda, "id", getPackageName());
            EditText currcell = (EditText) findViewById(id);
            dato[0] = currcell.getText().toString();

            celda = "editText" + Integer.toString(i+1);
            id = getResources().getIdentifier(celda, "id", getPackageName());
            currcell = (EditText) findViewById(id);
            dato[1] = currcell.getText().toString();

            celda = "editText" + Integer.toString(i+2);
            id = getResources().getIdentifier(celda, "id", getPackageName());
            currcell = (EditText) findViewById(id);
            dato[2] = currcell.getText().toString();

            // Si no hay evaluaciones nuevas Update, de lo contrario Insert
            if(k < evaluaciones.size()){
                if(!dato[0].equals("")){
                    SQLControlador repo = new SQLControlador(this);
                    EvaluacionPorCurso evaluacion = new EvaluacionPorCurso();
                    evaluacion.name = dato[0];
                    evaluacion.evaluacion = Double.parseDouble(dato[1]);
                    evaluacion.calificacion = Double.parseDouble(dato[2]);
                    evaluacion.curso_ID = Curso_Id;
                    evaluacion.evaluacion_ID = evaluaciones.get(k++);
                    repo.updateEvaluacion(evaluacion);
                }
            }else{
                if(!dato[0].equals("")){
                    SQLControlador repo = new SQLControlador(this);
                    EvaluacionPorCurso evaluacion = new EvaluacionPorCurso();
                    evaluacion.name = dato[0];
                    evaluacion.evaluacion = Double.parseDouble(dato[1]);
                    evaluacion.calificacion = Double.parseDouble(dato[2]);
                    evaluacion.curso_ID = Curso_Id;
                    repo.insertEvaluacion(evaluacion);
                }
            }

        }
        returnHome();
    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(),
                Cursos.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }
}
