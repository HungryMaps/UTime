/*
 * Created by Ana Laura and Jennifer on 2/10/2014.
 * Clase DbHelper: Se encarga de crear las tabla Cursos para la Base de Datos
 */

package com.example.android.utime.app;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

public class DBhelper extends SQLiteOpenHelper {

    private SQLiteDatabase mDb;
    private static final int DATABASE_VERSION = 8; //Maneja la version de la base de datos
    private static final String DATABASE_NAME = "crud.db"; // es el nombre de la base de datos

    public DBhelper(Context context ) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Metodo para crear las tablas en la base de datos
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_CURSO = "CREATE TABLE " + Curso.TABLE  + "("
                + Curso.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Curso.KEY_name + " TEXT, "
                + Curso.KEY_profesor + " TEXT, "
                + Curso.KEY_aula + " TEXT ,"
                + Curso.KEY_dias + " TEXT ,"
                + Curso.KEY_horas + " TEXT ,"
                + Curso.KEY_semestre + " TEXT ,"
                + Curso.KEY_anno + " TEXT )";

        String CREATE_TABLE_NOTA = "CREATE TABLE " + Nota.TABLE  + "("
                + Nota.KEY_ID_NOTA + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Nota.KEY_name_nota + " TEXT, "
                + Nota.KEY_comentario + " TEXT )";

        String CREATE_TABLE_EVALUACION = "CREATE TABLE " + EvaluacionPorCurso.TABLE  + "("
                + EvaluacionPorCurso.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EvaluacionPorCurso.KEY_name + " TEXT ,"
                + EvaluacionPorCurso.KEY_ID_Curso + " INTEGER , "
                + EvaluacionPorCurso.KEY_Calificacion + " TEXT,"
                + EvaluacionPorCurso.KEY_Evaluacion + " TEXT )";

        db.execSQL(CREATE_TABLE_CURSO);
        db.execSQL(CREATE_TABLE_EVALUACION);
        db.execSQL(CREATE_TABLE_NOTA);
    }

    /**
     * Metodo para actualizar la base de datos
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Curso.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Nota.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + EvaluacionPorCurso.TABLE);
        onCreate(db);
    }

    /**
     *
     *Coloca el cursor en la posicion del cuerpo d ela nota
     * @param rowId
     * @return
     * @throws SQLException
     */
    public Cursor fetchNote(long rowId) throws SQLException {

        Cursor mCursor = mDb.query(true, DATABASE_NAME, new String[] {Nota.KEY_ID_NOTA,
                        Nota.KEY_name_nota, Nota.KEY_comentario},Nota.KEY_ID_NOTA + "=" + rowId, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
}