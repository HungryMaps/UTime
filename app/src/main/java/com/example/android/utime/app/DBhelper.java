/*
 * Created by JENNIFER on 14/09/2014.
 * Clase DbHelper: Se encarga de crear las tabla Cursos para la Base de Datos
 */

package com.example.android.utime.app;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {

    // Información de la tabla Cursos
    public static final String TABLE_CURSOS = "cursos";
    public static final String CURSO_ID = "_id";
    public static final String CURSO_NOMBRE = "nombreCurso";

    // información de la a base de datos
    static final String DB_NAME = "DBCURSO";
    static final int DB_VERSION = 1;

    // Información de la base de datos
    private static final String CREATE_TABLE = "create table "
            + TABLE_CURSOS + "(" + CURSO_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CURSO_NOMBRE + " TEXT NOT NULL UNIQUE);";

    public DBhelper(Context context) {
        super(context, DB_NAME, null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURSOS);
        onCreate(db);
    }
}