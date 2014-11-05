package com.example.android.utime.app;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Evaluacion extends Activity implements android.view.View.OnClickListener{

    private int Curso_Id=0;
    private Button btnSave;
    private int contador = 1;
    private List<Integer> evaluaciones = new ArrayList<Integer>();

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
        TableLayout tabla = (TableLayout) findViewById(R.id.tablaEvaluacion);

        TableRow head = new TableRow(this);
        TextView columna = new TextView(this);

        columna.setText("Nombre");
        columna.setPadding(5,5,5,5);
        columna.setTypeface(null, Typeface.BOLD);
        columna.setTextSize(20);
        head.addView(columna);

        columna = new TextView(this);
        columna.setPadding(5,5,5,5);
        columna.setText("Evaluación(%)");
        columna.setTypeface(null, Typeface.BOLD);
        columna.setTextSize(20);
        head.addView(columna);

        columna = new TextView(this);
        columna.setPadding(5,5,5,5);
        columna.setText("Calificación");
        columna.setTypeface(null, Typeface.BOLD);
        columna.setTextSize(20);
        head.addView(columna);

        tabla.addView(head);

        // Se recorren todos los cursos obtenidos de la consulta de la base de datos y se crean dinámicamente los campos en el layout

        if (cursor.moveToFirst()) {
            int indice = 0;
            contador = 0;

              // Se presentan las evaluaciones en el layout
            do {

                evaluaciones.add(Integer.parseInt(cursor.getString(cursor.getColumnIndex(EvaluacionPorCurso.KEY_ID))));
                TableRow fila = new TableRow(this);

                EditText editText = new EditText(this);
                editText.setId(indice);
                editText.setHint("Examen, Quiz, Tarea, etc");
                editText.setText(cursor.getString(cursor.getColumnIndex(EvaluacionPorCurso.KEY_name)));
                fila.addView(editText);

                editText = new EditText(this);
                editText.setId(indice+1);
                editText.setHint("30 , 25 , etc");
                editText.setText(cursor.getString(cursor.getColumnIndex(EvaluacionPorCurso.KEY_Evaluacion)));
                fila.addView(editText);

                editText = new EditText(this);
                editText.setId(indice+2);
                editText.setHint("90 , 75 , etc");
                editText.setText(cursor.getString(cursor.getColumnIndex(EvaluacionPorCurso.KEY_Calificacion)));
                fila.addView(editText);

                if(contador==0){
                    Button Mas = new Button(this);
                    Mas.setId(R.id.Mas);
                    Mas.setOnClickListener(this);
                    Mas.setText("+");

                    Button Menos = new Button(this);
                    Menos.setId(R.id.Menos);
                    Menos.setOnClickListener(this);
                    Menos.setText("-");

                    fila.addView(Mas);
                    fila.addView(Menos);
                }

                tabla.addView(fila);

                contador++;
                indice+=3;
            } while (cursor.moveToNext());
        }else{ // Considera caso en que no haya ninguna evaluación ingresada en el curso

            TableRow fila = new TableRow(this);
            int ind = 0;

            EditText editText = new EditText(this);
            editText.setId(ind);
            editText.setHint("Examen, Quiz, Tarea, etc");
            fila.addView(editText);

            editText = new EditText(this);
            editText.setId(ind+1);
            editText.setHint("30 , 25 , etc");
            fila.addView(editText);

            editText = new EditText(this);
            editText.setId(ind+2);
            editText.setHint("90 , 75 , etc");
            fila.addView(editText);

            Button Mas = new Button(this);
            Mas.setId(R.id.Mas);
            Mas.setOnClickListener(this);
            Mas.setText("+");

            Button Menos = new Button(this);
            Menos.setId(R.id.Menos);
            Menos.setOnClickListener(this);
            Menos.setText("-");

            fila.addView(Mas);
            fila.addView(Menos);

            tabla.addView(fila);

            contador = 1;
        }
        this.CalcularNota();
    }

    /*
    *METODO QUE CALCULA LA NOTA, BASADO EN EL PORCENTAGE DE LAS EVALUACIONES
     */
    public void CalcularNota(){
        double notaFinal = 0;
        int j = 2;
        for(int i= 0; i < contador; i++){
            EditText currcell = (EditText) findViewById(j-1);
            String datoPorcentaje = currcell.getText().toString();
            if(!datoPorcentaje.equals("")){
                double porcentaje = Double.parseDouble(datoPorcentaje);
                currcell = (EditText) findViewById(j);
                String datoNota = currcell.getText().toString();
                double nota = Double.parseDouble(datoNota);
                notaFinal += (nota*porcentaje)/100;
            }
            j+=3;
        }
        TextView texto = (TextView) findViewById(R.id.Nota);
        texto.setText("NOTA: " + notaFinal);
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

    // Para cuando se apreta el botón de salvar, o el de + y -
    // llama al método correspondiente para reaccionar ante la acción
    @Override
    public void onClick(View view) {

        if(view == findViewById(R.id.btnSave)){
            Guardar();
        }
        else if(view ==  findViewById(R.id.Mas)){
            Agregar();
        }

        else if(view == findViewById(R.id.Menos)){
            Quitar();
        }
    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(),
                Cursos.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }

    /*
    * Método que guarda el contenido de los EditText que tienen los datos en la base local
    * También elimina de la base si corresponde
    * */

    public void Guardar(){

        int k=0;
        for(int i=0; i<contador*3; i+=3) {
            String[] dato = new String[3];

            // Saca datos del layout
            EditText currcell = (EditText) findViewById(i);
            dato[0] = currcell.getText().toString();

            currcell = (EditText) findViewById(i+1);
            dato[1] = currcell.getText().toString();

            currcell = (EditText) findViewById(i+2);
            dato[2] = currcell.getText().toString();

            //Toast.makeText(this, dato[0]+" "+dato[1]+" "+dato[2], Toast.LENGTH_LONG).show();

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

        //Ciclo para eliminar tuplas de la Base de Datos.

        int j = contador;
        for(int i=contador*3; i < evaluaciones.size()*3; i+=3) {
            SQLControlador repo = new SQLControlador(this);
            repo.deleteEvaluacion(evaluaciones.get(j++));
        }

        returnHome();
    }

    /**
     * Método para reaccionar ante la aumento de la cantidad de evaluaciones disponibles
     */
    public void Agregar(){
        TableLayout tabla = (TableLayout) findViewById(R.id.tablaEvaluacion);
        TableRow fila = new TableRow(this);

        EditText editText = new EditText(this);
        editText.setId(contador*3);
        editText.setHint("Examen, Quiz, Tarea, etc");
        fila.addView(editText);

        editText = new EditText(this);
        editText.setId(contador*3+1);
        editText.setHint("30 , 25 , etc");
        fila.addView(editText);

        editText = new EditText(this);
        editText.setId(contador*3+2);
        editText.setHint("90 , 75 , etc");
        fila.addView(editText);

        tabla.addView(fila);

        contador++;
    }

    /**
     * Método para reaccionar ante la reducción de la cantidad de evaluaciones disponibles
     */

    public void Quitar(){
        if(contador > 1) {
            TableLayout tabla = (TableLayout) findViewById(R.id.tablaEvaluacion);
            TableRow row = (TableRow) tabla.getChildAt(tabla.getChildCount()-1);
            row.removeAllViews();
            tabla.removeView(row);
            contador--;
        }else{
            Toast.makeText(this, "Número evaluciones mínimo", Toast.LENGTH_LONG).show();
        }
    }
}