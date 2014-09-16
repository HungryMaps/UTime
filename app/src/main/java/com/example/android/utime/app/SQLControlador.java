/**
 * Autores: Jennifer Ledezma
 *          Ana Laura Berdasco on 15/09/2014.
 * Clase SQLControlador: Se encarga de todas las operacioned de la BD (Insertar, Consultar, Actualizar,
 *                       Eliminar)
 */
package com.example.android.utime.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SQLControlador {

    private DbHelper dbhelper;
    private Context context;
    private SQLiteDatabase database;

    public SQLControlador(Context contexts) {
        context = contexts;
    }

    public SQLControlador abrirBaseDeDatos() throws SQLException {
        dbhelper = new DbHelper(context);
        database = dbhelper.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        dbhelper.close();
    }

    public void insertarDatos(String nombreCurso) {
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.CURSO_NOMBRE, nombreCurso);
        database.insert(DbHelper.TABLE_CURSO, null, cv);
    }

    public Cursor leerDatos() {
        String[] todasLasColumnas = new String[] {
                DbHelper.CURSO_ID,
                DbHelper.CURSO_NOMBRE
        };
        Cursor cursor = database.query(DbHelper.TABLE_CURSO, todasLasColumnas, null,
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
}
