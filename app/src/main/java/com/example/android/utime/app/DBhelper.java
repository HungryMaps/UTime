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

    /*
    * Información para la tabla de Notas
    */
    public static final String TABLE_NOTAS = "notas";
    public static final String NOTA_ID = "_id";
    public static final String NOTA_TEXTO = "notaTexto";

    // información de la a base de datos
    static final String DB_NAME = "DBCURSO";
    static final int DB_VERSION = 2;

    /*
    *  Importante! Se cambia la versión de la Base de Datos para poder agregar la tabla de notas
    *  Se cambia de 1 a 2.
     */

    // Información de la base de datos, para crear tabla de cursos
    private static final String CREATE_TABLE_CURSOS = "create table "
            + TABLE_CURSOS + "(" + CURSO_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CURSO_NOMBRE + " TEXT NOT NULL UNIQUE);";

    // Información de la base de datos, para crear la tabla de notas
    private static final String CREATE_TABLE_NOTAS = "create table "
            + TABLE_NOTAS + "(" + NOTA_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NOTA_TEXTO + " TEXT NOT NULL);";

    public DBhelper(Context context) {
        super(context, DB_NAME, null,DB_VERSION);
    }

    /*
    * Método para poder crear la base de datos, se corren los scrpits que se crearon antes,
    * por separado
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTAS);
        db.execSQL(CREATE_TABLE_CURSOS);
    }

    /*
    * Método para cuando se atcualiza la base
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURSOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTAS);
        onCreate(db);
    }
}