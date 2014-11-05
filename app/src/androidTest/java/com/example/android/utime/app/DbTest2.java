package com.example.android.utime.app;

import android.test.AndroidTestCase;

/**
 * Created by Joan Marchena on 03/11/14.
 */
public class DbTest2 extends AndroidTestCase {

    EvaluacionPorCurso evaluacion;

    public  void Setup1(){
        this.evaluacion = new EvaluacionPorCurso();
        evaluacion.curso_ID = 28;
        evaluacion.name = "Prueba";
        evaluacion.evaluacion = 30.00;
        evaluacion.calificacion = 100;
    }


    // Prueba el Insert en nota
    public void testInsertarNota()throws Throwable{
        Setup1();
        SQLControlador sql = new SQLControlador(mContext);
        int id = sql.insertEvaluacion(evaluacion);
        EvaluacionPorCurso evaluacion2 = sql.getEvaluacionByID(id);
        assertEquals("Prueba acertada",evaluacion.name, evaluacion2.name);
    }
}
