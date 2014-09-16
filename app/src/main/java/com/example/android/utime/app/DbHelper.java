package com.example.android.utime.app;
/**
 * Created by JENNIFER on 14/09/2014.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {


    public static final String TABLE_CURSO = "cursos";
    public static final String CURSO_ID = "_id";
    public static final String CURSO_NOMBRE = "nombreCurso";

    static final String DB_NAME = "DBCURSO";
    static final int DB_VERSION = 1;


    private static final String CREATE_TABLE_CURSO = "create table "
            + TABLE_CURSO + "(" + CURSO_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CURSO_NOMBRE + " TEXT NOT NULL UNIQUE);";

    public DbHelper(Context context) {
        super(context, DB_NAME, null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CURSO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURSO);
        onCreate(db);
    }
}
