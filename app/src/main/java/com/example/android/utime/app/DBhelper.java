/*
 * Created by JENNIFER on 14/09/2014.
 * Clase DbHelper: Se encarga de crear las tabla Cursos para la Base de Datos
 */

package com.example.android.utime.app;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 4;


    private static final String DATABASE_NAME = "crud.db";

    public DBhelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String CREATE_TABLE_CURSO = "CREATE TABLE " + Curso.TABLE  + "("
                + Curso.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Curso.KEY_name + " TEXT, "
                + Curso.KEY_profesor + " TEXT, "
                + Curso.KEY_aula + " TEXT ,"
                + Curso.KEY_dias + " TEXT ,"
                + Curso.KEY_horas + " TEXT )";

        db.execSQL(CREATE_TABLE_CURSO);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Curso.TABLE);


        onCreate(db);

    }

}