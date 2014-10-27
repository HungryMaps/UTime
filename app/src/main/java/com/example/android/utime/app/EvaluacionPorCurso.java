package com.example.android.utime.app;

/**
 * Created by David on 26/10/2014.
 */
public class EvaluacionPorCurso {
    // Creacion de la tabla
    public static final String TABLE = "Evaluacion";

    // Creacion de las columnas de la tabla
    public static final String KEY_ID = "id";
    public static final String KEY_name = "name";
    public static final String KEY_ID_Curso = "id_curso";
    public static final String KEY_Evaluacion = "evaluacion";
    public static final String KEY_Calificacion = "calificacion";


    // Ayuda a mantener los datos
    public int evaluacion_ID;
    public String name;
    public int curso_ID;
    public double evaluacion;
    public double calificacion;

}
