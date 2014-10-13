/*
 * Autor: Jennifer Ledezma
 * Clase Horario: Muestra el horario guardado en la app por el usuario, y opción de interactuar
 *               de distintas formas con este (crear un nuevo curso, editar, eliminar)
 */

package com.example.android.utime.app;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract.Events;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class Horario extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);

        // Se consulta la base de datos para ver los cursos que hay y sacar la información pertinente (horas, dias y nombre principalmente, para introducir datos en la tabla)
        DBhelper dbhelper = new DBhelper(getApplicationContext());
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String [] columnas =  { 
                Curso.KEY_ID,
                Curso.KEY_name,
                Curso.KEY_aula,
                Curso.KEY_dias,
                Curso.KEY_horas,
                Curso.KEY_semestre,
                Curso.KEY_anno
        };
        Cursor cursor = db.query(Curso.TABLE, columnas, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                    String QHoras = cursor.getString(cursor.getColumnIndex(Curso.KEY_horas));
                    String [] horas = QHoras.split(",");

                String anno = cursor.getString(cursor.getColumnIndex(Curso.KEY_anno));
                String semestre = cursor.getString(cursor.getColumnIndex(Curso.KEY_semestre));

                Time now = new Time();
                now.setToNow();
                if((semestre.equals("II") && now.month > 7 && now.year == Integer.parseInt(anno)) || (semestre.equals("I") && now.month < 7 && now.year == Integer.parseInt(anno))){
                    // Cada Columna de la tabla tiene un ID, dependiendo de la hora del curso corresponde a una fila en la tabla
                    // este switch saca esa fila
                    int tamano = horas.length/2;
                    int [] fila = new int[tamano];
                    for(int i=0; i< fila.length; i++) {
                        switch (Integer.parseInt(horas[2*i])) {
                            case 7:
                                fila[i] = 1;
                                break;
                            case 8:
                                fila[i] = 2;
                                break;
                            case 9:
                                fila[i] = 3;
                                break;
                            case 10:
                                fila[i] = 4;
                                break;
                            case 11:
                                fila[i] = 5;
                                break;
                            case 12:
                                fila[i] = 6;
                                break;
                            case 13:
                                fila[i] = 7;
                                break;
                            case 14:
                                fila[i] = 8;
                                break;
                            case 15:
                                fila[i] = 9;
                                break;
                            case 16:
                                fila[i] = 10;
                                break;
                            case 17:
                                fila[i] = 11;
                                break;
                            case 18:
                                fila[i] = 12;
                                break;
                            case 19:
                                fila[i] = 13;
                                break;
                            case 20:
                                fila[i] = 14;
                                break;
                            case 21:
                                fila[i] = 15;
                                break;
                            default:
                                break;
                        }
                    }
                    String QDias = cursor.getString(cursor.getColumnIndex(Curso.KEY_dias));
                    String [] dias = QDias.split(",");

                    // Mismo razonamiento del swtich anterior, pero para sacar la columna correspondiente al dia de la semana en la tabla

                    int [] columna = new int[dias.length];
                    for(int i=0; i<dias.length; i++){
                        if(dias[i].equals("Lunes")) {
                            columna[i] = 1;
                        }
                        if(dias[i].equals("Martes")) {
                            columna[i] = 2;
                        }
                        if(dias[i].equals("Miercoles")) {
                            columna[i] = 3;
                        }
                        if(dias[i].equals("Jueves")) {
                            columna[i] = 4;
                        }
                        if(dias[i].equals("Viernes")) {
                            columna[i] = 5;
                        }
                        if(dias[i].equals("Sabado")) {
                            columna[i] = 6;
                        }
                    }

                    // Inserta en la tabla
                    for(int i=0; i<columna.length; i++){
                        String [] indice = horas[2*i+1].split(":");
                        for(int j=0; j < Integer.parseInt(indice[0])+1 - Integer.parseInt(horas[2*i]); j++  ){
                            String celda = "celda"+Integer.toString(fila[i]+j)+Integer.toString(columna[i]);
                            int id = getResources().getIdentifier(celda, "id", getPackageName());
                            TextView currcell = (TextView) findViewById(id);
                            currcell.setText(cursor.getString(cursor.getColumnIndex(Curso.KEY_name)));
                        }
                    }
                }
            }while (cursor.moveToNext());
        }
    } //onCreate


    /*
        Este método se encarga de insertar los datos desde la base de datos hacia el calendario de modo que salgan los cursos que tengo que llevar en el semestre
        en el calendario, según la hora y días especificados
    */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void insertarHorarioEnCalendario(View view){

        //Consulta la base de datos
        DBhelper dbhelper = new DBhelper(getApplicationContext());
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String [] columnas =  {
                Curso.KEY_ID,
                Curso.KEY_name,
                Curso.KEY_aula,
                Curso.KEY_dias,
                Curso.KEY_horas,
                Curso.KEY_semestre,
                Curso.KEY_anno
        };

        Curso curso = new Curso();
        Cursor cursor = db.query(Curso.TABLE, columnas, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                // Datos de curso de la iteración
                curso.curso_ID =cursor.getInt(cursor.getColumnIndex(Curso.KEY_ID));
                curso.name =cursor.getString(cursor.getColumnIndex(Curso.KEY_name));
                curso.aula =cursor.getString(cursor.getColumnIndex(Curso.KEY_aula));
                curso.dias =cursor.getString(cursor.getColumnIndex(Curso.KEY_dias));
                curso.horas =cursor.getString(cursor.getColumnIndex(Curso.KEY_horas));
                curso.semestre =cursor.getString(cursor.getColumnIndex(Curso.KEY_semestre));
                curso.anno =cursor.getString(cursor.getColumnIndex(Curso.KEY_anno));

                long calID = 1;

                Calendar beginTime = null;
                Calendar endTime = null;
                TimeZone tz = TimeZone.getDefault();
                Time now = new Time();
                now.setToNow();
                String fechaFinal = "";
                String [] horas = curso.horas.split(",");
                String [] dias = curso.dias.split(",");

                for(int i=0; i < dias.length; i++) {

                    boolean entrar = false;

                    // Se verifica si el año y el semestre del curso corresponden al presente

                    if (now.month <= 6 && curso.semestre.equals("I") && now.year == Integer.parseInt(curso.anno)) {
                        beginTime = new GregorianCalendar(now.year, 2, 1);
                        endTime = new GregorianCalendar(now.year, 2, 1);
                        fechaFinal = now.year + "0620T180000Z";
                        entrar = true;
                    }
                    if (now.month > 7 && curso.semestre.equals("II") && now.year == Integer.parseInt(curso.anno)) {
                        beginTime = new GregorianCalendar(now.year, 7, 1);
                        endTime = new GregorianCalendar(now.year, 7, 1);
                        fechaFinal = now.year + "1120T210000Z";
                        entrar = true;
                    }

                    if (entrar) {
                        // Fecha del Evento a introducir en Calendario
                        beginTime.set(Calendar.HOUR, Integer.parseInt(horas[2*i]));
                        beginTime.set(Calendar.MINUTE, 0);
                        beginTime.set(Calendar.SECOND, 0);

                        String [] indice = horas[2*i+1].split(":");
                        endTime.set(Calendar.HOUR, Integer.parseInt(indice[0]));
                        endTime.set(Calendar.MINUTE, 50);
                        endTime.set(Calendar.SECOND, 0);
                        long startMillis = beginTime.getTimeInMillis();
                        long endMillis = endTime.getTimeInMillis();

                        String days = "";

                        // Clave según el día de la semana
                            if (dias[i].equals("Lunes")) {
                                days = "MO";
                            }
                            if (dias[i].equals("Martes")) {
                                days = "TU";
                            }
                            if (dias[i].equals("Miercoles")) {
                                days = "WE";
                            }
                            if (dias[i].equals("Jueves")) {
                                days = "TH";
                            }
                            if (dias[i].equals("Viernes")) {
                                days = "FR";
                            }
                            if (dias[i].equals("Sabado")) {
                                days = "SA";
                            }
                            if (dias[i].equals("Domingo")) {
                                days = "SU";
                            }

                        // Introduce datos a contenedor que se inserta como un evento en el calendario
                        ContentResolver cr = getContentResolver();
                        ContentValues values = new ContentValues();
                        values.put(Events.DTSTART, startMillis);
                        values.put(Events.DTEND, endMillis);
                        values.put(Events.TITLE, curso.name);
                        values.put(Events.CALENDAR_ID, calID);
                        values.put(Events.RRULE, "FREQ=WEEKLY;BYDAY=" + days + ";WKST=MO;UNTIL=" + fechaFinal);
                        values.put(Events.EVENT_TIMEZONE, tz.getID());

                        Uri uri = cr.insert(Events.CONTENT_URI, values);
                    }
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // las siguientes líneas sirven para borrar eventos del calendario, se deja aquí en caso de necesitarlo

        /*for(int i=0; i<1000; i++) {
            long eventID = i;
            Uri deleteUri = null;
            deleteUri = ContentUris.withAppendedId(Events.CONTENT_URI, eventID);
            int rows = getContentResolver().delete(deleteUri, null, null);
        }*/

    } //onCreate


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.horario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
