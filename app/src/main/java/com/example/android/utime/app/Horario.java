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

        DBhelper dbhelper = new DBhelper(getApplicationContext());
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String [] columnas =  {
                Curso.KEY_ID,
                Curso.KEY_name,
                Curso.KEY_aula,
                Curso.KEY_dias,
                Curso.KEY_horas
        };
        Cursor cursor = db.query(Curso.TABLE, columnas, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String QHoras = cursor.getString(cursor.getColumnIndex(Curso.KEY_horas));
                String [] horas = QHoras.split(",");

                int fila = 0;
                switch(Integer.parseInt(horas[0])){
                    case 7:
                        fila = 1;
                        break;
                    case 8:
                        fila = 2;
                        break;
                    case 9:
                        fila = 3;
                        break;
                    case 10:
                        fila = 4;
                        break;
                    case 11:
                        fila = 5;
                        break;
                    case 12:
                        fila = 6;
                        break;
                    case 13:
                        fila = 7;
                        break;
                    case 14:
                        fila = 8;
                        break;
                    case 15:
                        fila = 9;
                        break;
                    case 16:
                        fila = 10;
                        break;
                    case 17:
                        fila = 11;
                        break;
                    case 18:
                        fila = 12;
                        break;
                    case 19:
                        fila = 13;
                        break;
                    case 20:
                        fila = 14;
                        break;
                    case 21:
                        fila = 15;
                        break;
                    default:
                        break;
                }

                String QDias = cursor.getString(cursor.getColumnIndex(Curso.KEY_dias));
                String [] dias = QDias.split(",");

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

                for(int i=0; i<columna.length; i++){
                    String celda = "celda"+Integer.toString(fila)+Integer.toString(columna[i]);

                    int id = getResources().getIdentifier(celda, "id", getPackageName());
                    TextView currcell = (TextView) findViewById(id);
                    currcell.setText(cursor.getString(cursor.getColumnIndex(Curso.KEY_name)));
                }

            }while (cursor.moveToNext());
        }
    } //onCreate

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void insertarHorarioEnCalendario(View view){
        DBhelper dbhelper = new DBhelper(getApplicationContext());
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String [] columnas =  {
                Curso.KEY_ID,
                Curso.KEY_name,
                Curso.KEY_aula,
                Curso.KEY_dias,
                Curso.KEY_horas
        };

        Curso curso = new Curso();
        Cursor cursor = db.query(Curso.TABLE, columnas, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                curso.curso_ID =cursor.getInt(cursor.getColumnIndex(Curso.KEY_ID));
                curso.name =cursor.getString(cursor.getColumnIndex(Curso.KEY_name));
                curso.aula =cursor.getString(cursor.getColumnIndex(Curso.KEY_aula));
                curso.dias =cursor.getString(cursor.getColumnIndex(Curso.KEY_dias));
                curso.horas =cursor.getString(cursor.getColumnIndex(Curso.KEY_horas));

                long calID = 1;

                Calendar beginTime;
                Calendar endTime;
                TimeZone tz = TimeZone.getDefault();
                Time now = new Time();
                now.setToNow();
                String fechaFinal;
                String [] horas = curso.horas.split(",");
                String [] dias = curso.dias.split(",");
                if(now.month <= 6){
                    beginTime = new GregorianCalendar(now.year, 2, 1);
                    endTime = new GregorianCalendar(now.year, 2, 1);
                    fechaFinal = now.year+"0620T180000Z";
                }else{
                    beginTime = new GregorianCalendar(now.year, 7, 1);
                    endTime = new GregorianCalendar(now.year, 7, 1);
                    fechaFinal = now.year+"1120T180000Z";
                }


                beginTime.set(Calendar.HOUR, Integer.parseInt(horas[0]));
                beginTime.set(Calendar.MINUTE, 0);
                beginTime.set(Calendar.SECOND, 0);
                endTime.set(Calendar.HOUR, Integer.parseInt(horas[1]));
                endTime.set(Calendar.MINUTE, 50);
                endTime.set(Calendar.SECOND, 0);
                long startMillis = beginTime.getTimeInMillis();
                long endMillis = endTime.getTimeInMillis();

                String [] days = new String[dias.length];

                for(int i=0; i<dias.length; i++){
                    if(dias[i].equals("Lunes")){
                        days[i] = "MO";
                    }
                    if(dias[i].equals("Martes")){
                        days[i] = "TU";
                    }
                    if(dias[i].equals("Miercoles")){
                        days[i] = "WE";
                    }
                    if(dias[i].equals("Jueves")){
                        days[i] = "TH";
                    }
                    if(dias[i].equals("Viernes")){
                        days[i] = "FR";
                    }
                    if(dias[i].equals("Sabado")){
                        days[i] = "SA";
                    }
                    if(dias[i].equals("Domingo")){
                        days[i] = "SU";
                    }
                }

                String BYDAY = "";
                boolean seguir = true;
                int i=0;
                while(seguir){
                    BYDAY += days[i];
                    i++;
                    if(days.length == i){
                        seguir = false;
                    }else{
                        BYDAY += ",";
                    }
                }

                ContentResolver cr = getContentResolver();
                ContentValues values = new ContentValues();
                values.put(Events.DTSTART, startMillis);
                values.put(Events.DTEND, endMillis);
                values.put(Events.TITLE, curso.name);
                values.put(Events.CALENDAR_ID, calID);
                values.put(Events.RRULE,"FREQ=WEEKLY;BYDAY="+BYDAY+";WKST=MO;UNTIL="+fechaFinal);
                values.put(Events.EVENT_TIMEZONE, tz.getID());

                Uri uri = cr.insert(Events.CONTENT_URI, values);


            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        /*for(int i=0; i<1000; i++) {
            long eventID = i;
            Uri deleteUri = null;
            deleteUri = ContentUris.withAppendedId(Events.CONTENT_URI, eventID);
            int rows = getContentResolver().delete(deleteUri, null, null);
        }*/
    }


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
