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
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

public class SQLControlador {

    private DBhelper dbHelper;

    //Variables para la conexión con la base de datos externa
    private static String databaseBaseURL = "jdbc:mysql://Paula.db.4676399.hostedresource.com:3306/Paula";
    public String user = "Paula";
    public String pass = "Lopez123#";
    private Connection con;
    String resultado = "";

    public SQLControlador(Context context) {
        dbHelper = new DBhelper(context);
    }

    /**
     * Se encarga de hacer las inserciones en la tabla Curso
     * @param curso
     * @return
     */
    public int insert(Curso curso) {

        //Conneccion para escribir en la base
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Curso.KEY_horas, curso.horas);
        values.put(Curso.KEY_dias, curso.dias);
        values.put(Curso.KEY_aula, curso.aula);
        values.put(Curso.KEY_profesor,curso.profesor);
        values.put(Curso.KEY_name, curso.name);
        values.put(Curso.KEY_semestre, curso.semestre);
        values.put(Curso.KEY_anno, curso.anno);

        // Insertando filas
        long curso_Id = db.insert(Curso.TABLE, null, values);
        db.close(); // Cerrando la connecion de la base de datos

        //Base de datos externa
        InsertarCurso(curso);
        return (int) curso_Id;
    }

    /**
     * Se encarga de hacer las inserciones en la tabla Nota
     * @param nota
     * @return
     */
    public int insertNota(Nota nota) {

        //Conneccion para escribir en la base
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Nota.KEY_comentario, nota.comentarioNota);
        values.put(Nota.KEY_name_nota, nota.nameNota);

        // Insertando filas
        int nota_Id = (int) db.insert(Nota.TABLE, null, values);
        db.close(); // Cerrando la connecion de la base de datos

        //Base de datos externa
        InsertarNota(nota, nota_Id);
        return (int) nota_Id;
    }

    // Inserta Evaluacion
    public int insertEvaluacion(EvaluacionPorCurso evaluacion) {

        //Conneccion para escribir en la base
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EvaluacionPorCurso.KEY_ID_Curso, evaluacion.curso_ID);
        values.put(EvaluacionPorCurso.KEY_name, evaluacion.name);
        values.put(EvaluacionPorCurso.KEY_Evaluacion, evaluacion.evaluacion);
        values.put(EvaluacionPorCurso.KEY_Calificacion, evaluacion.calificacion);

        // Insertando filas
        int id = (int) db.insert(EvaluacionPorCurso.TABLE, null, values);
        db.close(); // Cerrando la connecion de la base de datos

        //Base de datos externa
        //InsertarNota(nota, nota_Id);
        return (int) id;
    }




    /**
     * Se encarga de eliminar un curso de la base de datos de manera temporal
     * @param curso_Id
     */



    public void delete(long curso_Id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Curso.TABLE, Curso.KEY_ID + "="
                + curso_Id, null);
        db.close();
    }

    /**
     * Se encarga de eliminar una nota de la base de datos de manera temporal
     * @param nota_Id
     */
    public void deleteNota(long nota_Id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Nota.TABLE, Nota.KEY_ID_NOTA + "="
                + nota_Id, null);
        db.close();
    }

    public void deleteEvaluacion (long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(EvaluacionPorCurso.TABLE, EvaluacionPorCurso.KEY_ID + "="
                + id, null);
        db.close();
    }

    /**
     * Efectúa la modificacion de datos  existentes de la tabla curso
     * @param curso
     */
    public void update(Curso curso) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Curso.KEY_anno, curso.anno);
        values.put(Curso.KEY_semestre, curso.semestre);
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
     * Efectúa la modificacion de datos  existentes de la tabla nota
     * @param nota
     */
    public void updateNota(Nota nota) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Nota.KEY_comentario,nota.comentarioNota);
        values.put(Nota.KEY_name_nota, nota.nameNota);

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(Nota.TABLE, values, Nota.KEY_ID_NOTA + "= ?", new String[] { String.valueOf(nota.nota_ID) });
        db.close(); // Closing database connection
    }

    // Actualiza Tabla con los datos de la evaluacion entrante como parámetro
    public void updateEvaluacion(EvaluacionPorCurso evaluacion) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EvaluacionPorCurso.KEY_name, evaluacion.name);
        values.put(EvaluacionPorCurso.KEY_Evaluacion, evaluacion.evaluacion);
        values.put(EvaluacionPorCurso.KEY_Calificacion, evaluacion.calificacion);

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(EvaluacionPorCurso.TABLE, values, EvaluacionPorCurso.KEY_ID + "= ?", new String[] { String.valueOf(evaluacion.evaluacion_ID) });
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
                Curso.KEY_horas + "," +
                Curso.KEY_semestre + "," +
                Curso.KEY_anno +
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
     * Obtiene la lista de Notas
     * @return
     */
    public ArrayList<HashMap<String, String>>  getNotaList() {
        //Abrir la base en modo read-only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Nota.KEY_ID_NOTA + "," +
                Nota.KEY_name_nota + "," +
                Nota.KEY_comentario +
                " FROM " + Nota.TABLE;

        ArrayList<HashMap<String, String>> notaList = new ArrayList<HashMap<String, String>>();
        Cursor cursor = db.rawQuery(selectQuery, null);// Se agrega a la lista

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> nota = new HashMap<String, String>();
                nota.put("idNota", cursor.getString(cursor.getColumnIndex(Nota.KEY_ID_NOTA)));
                nota.put("nameNota", cursor.getString(cursor.getColumnIndex(Nota.KEY_name_nota)));
                notaList.add(nota);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notaList;
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
                Curso.KEY_horas + "," +
                Curso.KEY_semestre + "," +
                Curso.KEY_anno +
                " FROM " + Curso.TABLE
                + " WHERE " +
                Curso.KEY_ID + "=?";                              //se usa '?' para concatenar strings

        Curso curso = null;
        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            curso = new Curso();
            do {
                curso.curso_ID =cursor.getInt(cursor.getColumnIndex(Curso.KEY_ID));
                curso.name =cursor.getString(cursor.getColumnIndex(Curso.KEY_name));
                curso.profesor  =cursor.getString(cursor.getColumnIndex(Curso.KEY_profesor));
                curso.aula =cursor.getString(cursor.getColumnIndex(Curso.KEY_aula));
                curso.dias =cursor.getString(cursor.getColumnIndex(Curso.KEY_dias));
                curso.horas =cursor.getString(cursor.getColumnIndex(Curso.KEY_horas));
                curso.semestre =cursor.getString(cursor.getColumnIndex(Curso.KEY_semestre));
                curso.anno =cursor.getString(cursor.getColumnIndex(Curso.KEY_anno));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return curso;
    }
    /**
     * Metodo para obtener la nota segun el id
     * @param Id
     * @return
     */
    public Nota getNotaById(int Id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Nota.KEY_ID_NOTA + "," +
                Nota.KEY_name_nota + "," +
                Nota.KEY_comentario +
                " FROM " + Nota.TABLE
                + " WHERE " +
                Nota.KEY_ID_NOTA + "=?";                              //se usa '?' para concatenar strings

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );
        Nota nota = null;

        if (cursor.moveToFirst()) {
            nota = new Nota();
            do {
                nota.nota_ID =cursor.getInt(cursor.getColumnIndex(Nota.KEY_ID_NOTA));
                nota.nameNota =cursor.getString(cursor.getColumnIndex(Nota.KEY_name_nota));
                nota.comentarioNota  =cursor.getString(cursor.getColumnIndex(Nota.KEY_comentario));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return nota;
    }

    /*
    * Método que llama a la conexión de la base
    */

    public void InsertarNota(Nota nota, int nota_Id) {
        ConnectNotas task = new ConnectNotas(nota, nota_Id);
        task.execute();
    }

    /*
    * Método que llama a la conexión de la base
    */

    public void InsertarCurso(Curso curso) {
        ConnectCurso task = new ConnectCurso(curso);
        task.execute();
    }
}