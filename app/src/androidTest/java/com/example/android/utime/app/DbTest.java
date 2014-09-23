package com.example.android.utime.app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * Created by JENNIFER on 23/09/2014.
 */

public class DbTest extends AndroidTestCase {

    public static final String LOG_TAG = DbTest.class.getSimpleName();

    public void testPrueba()throws Throwable{
        mContext.deleteDatabase(DBhelper.DB_NAME);
        SQLiteDatabase db = new DBhelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testInsert()throws Throwable{
        String name = "Inge";
        mContext.deleteDatabase(DBhelper.DB_NAME);
        SQLiteDatabase db = new DBhelper(this.mContext).getWritableDatabase();

        //ContentValues cv = new ContentValues();
        //cv.put(DBhelper.CURSO_NOMBRE, name);
        //db.insert(DBhelper.TABLE_CURSOS, null, cv);
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

        assertEquals("pase yay", nombre, name);
        //assertFalse(nombre, 1<0);
        db.close();

    }
}


