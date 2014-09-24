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

    /**
     * Prueba que verifica si se agrega bien una nota a la tabla de notas
     */
    public void testInsertarNota()throws Throwable{
        //La nota contiene "Ana 88888888" como si fuera un número de teléfono
        String nota = "Ana 88888888";
        mContext.deleteDatabase(DBhelper.DB_NAME);
        SQLiteDatabase db = new DBhelper(this.mContext).getWritableDatabase();

        SQLControlador controlador = new SQLControlador(mContext);
        controlador.abrirBaseDeDatos();
        //Se inserta en la base
        controlador.insertarDatosNotas(nota);

        //Aquí vamos a revisar si se insertó bien esa nota
        Cursor cursor = controlador.leerDatosNotas();

        int textoNota = cursor.getColumnIndex(DBhelper.NOTA_TEXTO);
        String texto = cursor.getString(textoNota);
        Log.d(LOG_TAG, "PRUEBA "+texto);

        assertEquals("Prueba correcta", texto, nota);
        db.close();
    }
    
    /**
     * Elimina una nota de la base de datos
     * @throws Throwable
     */
    public void testDeleteNota()throws Throwable{
        String name = "Ensamblador";
        mContext.deleteDatabase(DBhelper.DB_NAME);
        SQLiteDatabase db = new DBhelper(this.mContext).getWritableDatabase();

        SQLControlador ctrl = new SQLControlador(mContext);
        ctrl.abrirBaseDeDatos();
        ctrl.insertarDatosNotas(name);
        Cursor cursor = ctrl.leerDatosNotas();

        int idnota = cursor.getColumnIndex(DBhelper.NOTA_ID); //para tomar el id
        int nombreC = cursor.getColumnIndex(DBhelper.NOTA_TEXTO);
        String nombre = cursor.getString(nombreC);
        String id = cursor.getString(idnota);
        long idnum = Integer.parseInt(id);
        ctrl.deleteData(idnum); //elimina la nota
        Cursor  cursor2 = ctrl.leerDatosNotas();
        nombreC = cursor.getColumnIndex(DBhelper.NOTA_TEXTO);
       assertNotSame("Prueba delete",cursor,cursor2);
      //assertEquals("Prueba acertada", nombre, name); //esta prueba tambien sirve y la intencion es que falle
        db.close();
    }

    /**
     * Elimina un curso de la base de datos
     * @throws Throwable
     */
    public void testDeleteCurso()throws Throwable{
        String name = "Ensamblador";
        mContext.deleteDatabase(DBhelper.DB_NAME);
        SQLiteDatabase db = new DBhelper(this.mContext).getWritableDatabase();

        SQLControlador ctrl = new SQLControlador(mContext);
        ctrl.abrirBaseDeDatos();
        ctrl.insertarDatos(name);
        Cursor cursor = ctrl.leerDatos();

        int idcurso = cursor.getColumnIndex(DBhelper.CURSO_ID); //para tomar el id
        int nombreC = cursor.getColumnIndex(DBhelper.CURSO_NOMBRE);
        String nombre = cursor.getString(nombreC);
        String id = cursor.getString(idcurso);
        long idnum = Integer.parseInt(id);
        ctrl.deleteData(idnum);// elimina el curso de la base
        Cursor  cursor2 = ctrl.leerDatos();
        assertNotSame("Prueba delete",cursor,cursor2);

        db.close();
    }

    /**
     * Prueba que verifica que se actualizo el nombre del curso
     * @throws Throwable
     */
    public void testModificarCurso()throws Throwable{
        String name = "Ensamblador";
        mContext.deleteDatabase(DBhelper.DB_NAME);
        SQLiteDatabase db = new DBhelper(this.mContext).getWritableDatabase();

        SQLControlador ctrl = new SQLControlador(mContext);
        ctrl.abrirBaseDeDatos();
        ctrl.insertarDatos(name);
        Cursor cursor = ctrl.leerDatos();

        int idcurso = cursor.getColumnIndex(DBhelper.CURSO_ID); //para tomar el id
        String id = cursor.getString(idcurso);
        long idnum = Integer.parseInt(id);
        String nuevoNombre = "Estructuras Discretas";

        ctrl.actualizarDatos(idnum,nuevoNombre);
        cursor = ctrl.leerDatos();
        int nombreC = cursor.getColumnIndex(DBhelper.CURSO_NOMBRE);
        String nombre = cursor.getString(nombreC);
        assertEquals("Prueba Modifica Curso",nombre,nuevoNombre );

        db.close();
    }

}


