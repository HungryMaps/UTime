package com.example.android.utime.app;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityUnitTestCase;
import android.widget.TextView;

/**
 * Created by Joan Marchena on 03/11/14.
 * Se prueba el Activity
 */

/*
* Prueba para la activity de Evaluacion. Próposito: probar algunas funcionalidades (métodos) de la activity
* */
public class EvaluacionTest extends ActivityUnitTestCase<Evaluacion> {
    private Evaluacion mTestActivity;
    private TextView mTestText;

    public EvaluacionTest() {
        super(Evaluacion.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Se Inserta una evaluación de prueba con ID 0 ya que es el default que toma en la activity

        EvaluacionPorCurso evaluacion = new EvaluacionPorCurso();
        evaluacion.curso_ID = 0;
        evaluacion.name = "Prueba";
        evaluacion.evaluacion = 30;
        evaluacion.calificacion = 100;

        DBhelper dbHelper = new DBhelper(getInstrumentation().getTargetContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                EvaluacionPorCurso.KEY_ID + "," +
                EvaluacionPorCurso.KEY_ID_Curso + "," +
                EvaluacionPorCurso.KEY_Evaluacion +  "," +
                EvaluacionPorCurso.KEY_Calificacion + "," +
                EvaluacionPorCurso.KEY_name +
                " FROM " + EvaluacionPorCurso.TABLE
                + " WHERE " +
                EvaluacionPorCurso.KEY_ID_Curso + "=?";

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(0) } );
        if(!cursor.moveToFirst()){
            SQLControlador sql = new SQLControlador(getInstrumentation().getTargetContext());
            sql.insertEvaluacion(evaluacion);
        }

        // Se inicia la Activity
        startActivity(new Intent(getInstrumentation().getTargetContext(), Evaluacion.class), null, null);

        // Obtenemos la referencia para usar los métodos
        mTestActivity = (Evaluacion) getActivity();

        // Refencia al TextView de Nota que se pone en la Activity
        mTestText = (TextView) mTestActivity.findViewById(R.id.Nota);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /*
    *Prueba para un TextView del Activity de Evaluacion que muestra la nota obtenida según las evaluaciones
     */
    public void testNota() {
        String actual = mTestText.getText().toString();
        // Se es espera un 30 con los datos insertados en el setUp
        String expected = "NOTA: 30.0";
        assertEquals(expected, actual);
    }


}

