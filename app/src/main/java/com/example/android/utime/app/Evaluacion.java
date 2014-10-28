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
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Evaluacion extends ActionBarActivity implements android.view.View.OnClickListener{

    private int Curso_Id=0;
    Button btnSave;
    int contador = 1;
    int contadorInicial = 0;
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
            contador = 0;
            // Se presentan las evaluaciones en el layout
            do {

                evaluaciones.add(Integer.parseInt(cursor.getString(cursor.getColumnIndex(EvaluacionPorCurso.KEY_ID))));

                String celda = "editText"+Integer.toString(i);
                int id = getResources().getIdentifier(celda, "id", getPackageName());
                EditText currcell = (EditText) findViewById(id);
                currcell.setText(cursor.getString(cursor.getColumnIndex(EvaluacionPorCurso.KEY_name)));
                currcell.setVisibility(View.VISIBLE);

                celda = "editText"+Integer.toString(i+1);
                id = getResources().getIdentifier(celda, "id", getPackageName());
                currcell = (EditText) findViewById(id);
                currcell.setText(cursor.getString(cursor.getColumnIndex(EvaluacionPorCurso.KEY_Evaluacion)));
                currcell.setVisibility(View.VISIBLE);

                celda = "editText"+Integer.toString(i+2);
                id = getResources().getIdentifier(celda, "id", getPackageName());
                currcell = (EditText) findViewById(id);
                currcell.setText(cursor.getString(cursor.getColumnIndex(EvaluacionPorCurso.KEY_Calificacion)));
                currcell.setVisibility(View.VISIBLE);

                i+=3;
                contador++;
            } while (cursor.moveToNext());
        }else{
            contador = 1;
        }
        contadorInicial = contador;
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

        /*
        *Ciclo para eliminar tuplas de la Base de Datos.
         */

        int j = contador;
        for(int i=2+contador*3; i<29; i+=3) {
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

            if(j < evaluaciones.size()) {
               SQLControlador repo = new SQLControlador(this);
               repo.deleteEvaluacion(evaluaciones.get(j++));
            }
        }

        returnHome();
    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(),
                Cursos.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }

    /**
     * Método para reaccionar ante la aumento de la cantidad de evaluaciones disponibles
     * @param view
     */
    public void Agregar(View view){
        if(contador < 10) {
            for(int i=0;i<3;i++) {
                String editText = "editText" + Integer.toString((contador*3)+2+i);
                int id = getResources().getIdentifier(editText, "id", getPackageName());
                EditText local = (EditText) findViewById(id);
                local.setVisibility(View.VISIBLE);
            }
            contador++;
        }else{
            Toast.makeText(this, "Numero de evaluaciones máximo", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Método para reaccionar ante la reducción de la cantidad de evaluaciones disponibles
     * @param view
     */

    public void Quitar(View view){
        if(contador > 1) {
            for(int i=0;i<3;i++) {
                String editText = "editText" + Integer.toString((contador*3)-1+i);
                int id = getResources().getIdentifier(editText, "id", getPackageName());
                EditText local = (EditText) findViewById(id);
                local.setVisibility(View.INVISIBLE);
            }
            contador--;
        }else{
            Toast.makeText(this, "Número evaluciones mínimo", Toast.LENGTH_LONG).show();
        }
    }
}
