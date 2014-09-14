/*
 * Autor: Jennifer Ledezma
 * Clase Cursos: Muestra los cursos guardados en la app por el usuario, y opción de interactuar
 *               de distintas formas con estos (crear un nuevo curso, editar, eliminar)
 */

package com.example.android.utime.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class CrearCurso {

    public static final String TABLE_NAME = "Cursos";
    public static final String CURSO_ID = "_id";
    public static final String CURSO_NAME = "nombreCur";
    public static final String CURSO_DIA_I = "diaInicial";
    public static final String CURSO_DIA_F = "diaFinal";
    public static final String CURSO_PROFESOR = "nombreProf";
    public static final String CURSO_FACULTAD = "facultad";
    public static final String CURSO_AULA = "aula";

    public static final String CREATE_TABLE = "create table " +TABLE_NAME+ " ("
            + CURSO_ID + " integer primary key autoincrement,"
            + CURSO_NAME + " text not null unique,"
            + CURSO_DIA_I + " text not null,"
            + CURSO_DIA_F + " text not null,"
            + CURSO_PROFESOR + " text,"
            + CURSO_FACULTAD + " text,"
            + CURSO_AULA + " text);";

    private DbHelper helper;
    private SQLiteDatabase db;

    public CrearCurso(Context context){
         helper = new DbHelper(context);
         db = helper.getWritableDatabase();
    }

    public ContentValues generarContentValues(String nombreCur, String diaInicial, String diaFinal,
                                              String nombreProf, String facultad, String aula){
        ContentValues valores = new ContentValues();
        valores.put(CURSO_NAME, nombreCur);
        valores.put(CURSO_DIA_I, diaInicial);
        valores.put(CURSO_DIA_F, diaFinal);
        valores.put(CURSO_PROFESOR, nombreProf);
        valores.put(CURSO_FACULTAD, facultad);
        valores.put(CURSO_AULA, aula);

        return valores;
    }

    public void insertar(String nombreCur, String diaInicial, String diaFinal,
                         String nombreProf, String facultad, String aula){
        db.insert(TABLE_NAME, null, generarContentValues(nombreCur, diaInicial,diaFinal, nombreProf, facultad, aula));
    }



}
