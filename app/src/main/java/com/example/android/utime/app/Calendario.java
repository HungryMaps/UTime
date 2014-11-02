/*
 * Autor: Paula López
 * Clase Calendario: Muestra el calendario de la aplicación, además de un espacio para notas generales
 *                  que se quieran hacer.
 */


package com.example.android.utime.app;


import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class Calendario extends ActionBarActivity {

    /*
    *  Método para crear el activity cuando lo llama menú.
    */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);
        String[] projection =
                new String[]{
                        Calendars._ID,
                        Calendars.NAME,
                        Calendars.ACCOUNT_NAME,
                        Calendars.ACCOUNT_TYPE};
        Cursor calCursor =
                getContentResolver().
                        query(Calendars.CONTENT_URI,
                                projection,
                                Calendars.VISIBLE + " = 1",
                                null,
                                Calendars._ID + " ASC");
        if (calCursor.moveToFirst()) {
            do {
                long id = calCursor.getLong(0);
                String displayName = calCursor.getString(1);
                Toast.makeText(this, "Calendar " + displayName + " " + id, Toast.LENGTH_SHORT).show();
            } while (calCursor.moveToNext());
        }
        insertarFechasEnCalendario();
//        Intent intent = new Intent(this, FechasImportantes.class);
  //      startActivity(intent);

    }

    /*
    * Método para insertar un evento nuevo en el calendario
    * Trae un poco de información preestablecida
    * */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(Events.TITLE, "Info");
        intent.putExtra(Events.EVENT_LOCATION, "Universidad de Costa Rica");
        intent.putExtra(Events.DESCRIPTION, "Ingrese Descripción");

        Time now = new Time();
        now.setToNow();
        GregorianCalendar calDate = new GregorianCalendar(now.year, now.month, now.monthDay, now.hour, now.minute, now.second);
        GregorianCalendar calEnd = new GregorianCalendar(now.year, now.month, now.monthDay, now.hour+2, now.minute, now.second);

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                calDate.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                calEnd.getTimeInMillis());

        startActivity(intent);

    }


    /*
    * Método para ver el estado del dia de hoy en el calendario
    * */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void queryCalendar(View view) {
        Time now = new Time();
        now.setToNow();
        long startMillis = now.toMillis(true);
        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        ContentUris.appendId(builder, startMillis);
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setData(builder.build());
        startActivity(intent);
    }

    // Las siguientes líneas son para Consultar el ID y nombre del calendario (cuenta asociada)
    // Se deja aquí en caso de necesitarlo para alguna verificación en el futuro

    /*String[] projection =
                new String[]{
                        Calendars._ID,
                        Calendars.NAME,
                        Calendars.ACCOUNT_NAME,
                        Calendars.ACCOUNT_TYPE};
        Cursor calCursor =
                getContentResolver().
                        query(Calendars.CONTENT_URI,
                                projection,
                                Calendars.VISIBLE + " = 1",
                                null,
                                Calendars._ID + " ASC");
        if (calCursor.moveToFirst()) {
            do {
                long id = calCursor.getLong(0);
                String displayName = calCursor.getString(1);
                Toast.makeText(this, "Calendar " + displayName + " " +id, Toast.LENGTH_SHORT).show();
            } while (calCursor.moveToNext());
        }*/

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void insertarFechasEnCalendario() {

        //Consulta la base de datos

        /*
        Traer de la base de datos remota
         */

        long calID = 1;

        Calendar beginTime = null;
        Calendar endTime = null;
        TimeZone tz = TimeZone.getDefault();
        Time now = new Time();
        now.setToNow();

        // Se verifica si el año y el semestre del curso corresponden al presente

        //Este 2 viene de la base : mes
        //Este 1 viene de la base: dia
        beginTime = new GregorianCalendar(now.year, 2, 1);
        endTime = new GregorianCalendar(now.year, 2, 1);

        // Fecha del Evento a introducir en Calendario
        beginTime.set(Calendar.HOUR, 8);
        beginTime.set(Calendar.MINUTE, 0);
        beginTime.set(Calendar.SECOND, 0);
        endTime.set(Calendar.HOUR, 18);
        endTime.set(Calendar.MINUTE, 0);
        endTime.set(Calendar.SECOND, 0);
        long startMillis = beginTime.getTimeInMillis();
        long endMillis = endTime.getTimeInMillis();

        // Introduce datos a contenedor que se inserta como un evento en el calendario
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, "HOLA ENFERMERA");
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, tz.getID());

        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);


        // las siguientes líneas sirven para borrar eventos del calendario, se deja aquí en caso de necesitarlo

        /*for(int i=0; i<1000; i++) {
            long eventID = i;
            Uri deleteUri = null;
            deleteUri = ContentUris.withAppendedId(Events.CONTENT_URI, eventID);
            int rows = getContentResolver().delete(deleteUri, null, null);
        }*/

    } //onCreate
}
