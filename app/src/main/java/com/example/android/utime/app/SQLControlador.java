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

import java.util.ArrayList;
import java.util.HashMap;

public class SQLControlador {

    private DBhelper dbHelper;

    public SQLControlador(Context context) {
        dbHelper = new DBhelper(context);
    }

    public int insert(Curso curso) {

        //Conneccion para escribir en la base
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Curso.KEY_horas, curso.horas);
        values.put(Curso.KEY_dias, curso.dias);
        values.put(Curso.KEY_aula, curso.aula);
        values.put(Curso.KEY_profesor,curso.profesor);
        values.put(Curso.KEY_name, curso.name);

        // Insertando filas
        long curso_Id = db.insert(Curso.TABLE, null, values);
        db.close(); // Cerrando la connecion de la base de datos
        return (int) curso_Id;
    }

    /**
     * Se encarga de eliminar un curso de la base de datos de manera temporal
     * @param curso_Id
     */
    public void delete(int curso_Id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(Curso.TABLE, Curso.KEY_ID + "= ?", new String[] { String.valueOf(curso_Id) });
        db.close(); // Closing database connection
    }

    /**
     * Efectúa la modificacion de datos  existentes de la tabla curso
     * @param curso
     */
    public void update(Curso curso) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Curso.KEY_horas, curso.horas);
        values.put(Curso.KEY_dias, curso.dias);
        values.put(Curso.KEY_aula, curso.aula);
        values.put(Curso.KEY_profesor,curso.profesor);
        values.put(Curso.KEY_name, curso.name);

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(Curso.TABLE, values, Curso.KEY_ID + "= ?", new String[] { String.valueOf(curso.curso_ID) });
        db.close(); // Closing database connection
    }

    /**
     * Obtiene la lista de cursos
     * @return
     */
    public ArrayList<HashMap<String, String>>  getCursoList() {
        //Abrir la base en modo read-only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Curso.KEY_ID + "," +
                Curso.KEY_name + "," +
                Curso.KEY_profesor + "," +
                Curso.KEY_aula + "," +
                Curso.KEY_dias + "," +
                Curso.KEY_horas +
                " FROM " + Curso.TABLE;

        //Curso curso = new Curso();
        ArrayList<HashMap<String, String>> cursoList = new ArrayList<HashMap<String, String>>();
        Cursor cursor = db.rawQuery(selectQuery, null);// Se agrega a la lista

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> curso = new HashMap<String, String>();
                curso.put("id", cursor.getString(cursor.getColumnIndex(Curso.KEY_ID)));
                curso.put("name", cursor.getString(cursor.getColumnIndex(Curso.KEY_name)));
                cursoList.add(curso);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cursoList;
    }

    /**
     * Metodo para obtener el curso segun el id
     * @param Id
     * @return
     */
    public Curso getCursoById(int Id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Curso.KEY_ID + "," +
                Curso.KEY_name + "," +
                Curso.KEY_profesor + "," +
                Curso.KEY_aula  + "," +
                Curso.KEY_dias + "," +
                Curso.KEY_horas  +
                " FROM " + Curso.TABLE
                + " WHERE " +
                Curso.KEY_ID + "=?";                              //se usa '?' para concatenar strings

        Curso curso = new Curso();
        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                curso.curso_ID =cursor.getInt(cursor.getColumnIndex(Curso.KEY_ID));
                curso.name =cursor.getString(cursor.getColumnIndex(Curso.KEY_name));
                curso.profesor  =cursor.getString(cursor.getColumnIndex(Curso.KEY_profesor));
                curso.aula =cursor.getString(cursor.getColumnIndex(Curso.KEY_aula));
                curso.dias =cursor.getString(cursor.getColumnIndex(Curso.KEY_dias));
                curso.horas =cursor.getString(cursor.getColumnIndex(Curso.KEY_horas));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return curso;
    }
}