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


public class EvaluacionTest extends ActivityUnitTestCase<Evaluacion> {
    private Evaluacion mTestActivity;
    private TextView mTestText;

    public EvaluacionTest() {
        super(Evaluacion.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

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

        // Starts the MainActivity of the target application
        startActivity(new Intent(getInstrumentation().getTargetContext(), Evaluacion.class), null, null);

        // Getting a reference to the MainActivity of the target application
        mTestActivity = (Evaluacion) getActivity();

        // Getting a reference to the TextView of the MainActivity of the target application
        mTestText = (TextView) mTestActivity.findViewById(R.id.Nota);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /*
    *Prueba para un TextView del Activity de Evaluacion
     */
    public void testNota() {
        // The actual text displayed in the textview
        String actual = mTestText.getText().toString();

        // The expected text to be displayed in the textview
        String expected = "NOTA";

        // Check whether both are equal, otherwise test fails
        assertEquals(expected, actual.substring(0, 4));
    }


}

