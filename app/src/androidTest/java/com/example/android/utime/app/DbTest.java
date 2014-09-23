package com.example.android.utime.app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * Created by JENNIFER on 23/09/2014.
 * Clase que se encarga de hacerle pruebas unitarias a la BD en la tabla de Curso
 */

public class DbTest extends AndroidTestCase {

    public static final String LOG_TAG = DbTest.class.getSimpleName();

    /**
     * Limpia la base de datos y prueba si la está creando y si la puede abrir
     * @throws Throwable
     */
    public void testPrueba()throws Throwable{
        mContext.deleteDatabase(DBhelper.DB_NAME);
        SQLiteDatabase db = new DBhelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    /**
     * Prueba Unitaria para hacer el Insertar en la DB
     * @throws Throwable
     */
    public void testInsert()throws Throwable{
        String name = "Ingeniería del Software";
        mContext.deleteDatabase(DBhelper.DB_NAME);
        SQLiteDatabase db = new DBhelper(this.mContext).getWritableDatabase();

        SQLControlador ctrl = new SQLControlador(mContext);
        ctrl.abrirBaseDeDatos();
        ctrl.insertarDatos(name);
        Cursor cursor = ctrl.leerDatos();
        String[] cadena;
        cadena = new String[] {
                DBhelper.CURSO_ID,
                DBhelper.CURSO_NOMBRE
        };
        int nombreC = cursor.getColumnIndex(DBhelper.CURSO_NOMBRE);
        String nombre = cursor.getString(nombreC);
        Log.d(LOG_TAG, "PRUEBA "+nombre);

        assertEquals("Prueba acertada", nombre, name);
        db.close();
    }
}


