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
import android.database.sqlite.SQLiteDatabase;
import android.database.SQLException;

public class SQLControlador {

    private DBhelper dbhelper;
    private Context context;
    private SQLiteDatabase database;

    public SQLControlador(Context contexts) {
        context = contexts;
    }

    public SQLControlador abrirBaseDeDatos() throws SQLException {
        dbhelper = new DBhelper(context);
        database = dbhelper.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        dbhelper.close();
    }

    public void insertarDatos(String name) {
        ContentValues cv = new ContentValues();
        cv.put(DBhelper.CURSO_NOMBRE, name);
        database.insert(DBhelper.TABLE_CURSOS, null, cv);
    }

    public Cursor leerDatos() {
        String[] todasLasColumnas = new String[] {
                DBhelper.CURSO_ID,
                DBhelper.CURSO_NOMBRE
        };
        Cursor cursor = database.query(DBhelper.TABLE_CURSOS, todasLasColumnas, null,
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    /**
     * Actualiza los datos en la base de datos
     * @param memberID
     * @param cursoName
     * @return actualizado
     */
    public int actualizarDatos(long memberID, String cursoName) {
        ContentValues cvActualizar = new ContentValues();
        cvActualizar.put(DBhelper.CURSO_NOMBRE, cursoName);
        int actualizado = database.update(DBhelper.TABLE_CURSOS, cvActualizar,
                DBhelper.CURSO_ID + " = " + memberID, null);
        return actualizado;
    }

    /**
     * Elimina los datos de la base de datos
     * @param cursoID
     */
    public void deleteData(long cursoID) {
        database.delete(DBhelper.TABLE_CURSOS, DBhelper.CURSO_ID + "="
                + cursoID, null);
    }
}