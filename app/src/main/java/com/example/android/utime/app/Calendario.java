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
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

    private String[] fechas = new String[300];
    ConnectFecha task;
    private int fechasActualizadas = 1;

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
        GregorianCalendar calEnd = new GregorianCalendar(now.year, now.month, now.monthDay, now.hour + 2, now.minute, now.second);

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

    /**
     * Método para revisar si hay conexión a internet
     * @return boolean
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /*
    * Método para llenar el calendario con los eventos de fechas preestablecidas.
    */
    public void llenarFechas(View view) {
        if(this.isNetworkAvailable()) { //Si hay conexión, se va a la base remota
            if (fechasActualizadas == 1) {
                insertarFecha();
                fechasActualizadas--;
                Toast.makeText(this, "Se agregaron fechas preestablecidas al calendario", Toast.LENGTH_LONG).show();
            }
        }
        else{ //Sino, entonces se le dice al usuario
            Toast.makeText(this, "NO HAY CONEXION A INTERNET", Toast.LENGTH_LONG).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void insertarFechasEnCalendario(int dia, String titulo, int mes) {
        long calID = 1;

        Calendar beginTime = null;
        Calendar endTime = null;
        TimeZone tz = TimeZone.getDefault();
        Time now = new Time();
        now.setToNow();

        beginTime = new GregorianCalendar(now.year, mes, dia);
        endTime = new GregorianCalendar(now.year, mes, dia);

        // Fecha del Evento a introducir en Calendario
        beginTime.set(Calendar.HOUR, 8);
        beginTime.set(Calendar.MINUTE, 0);
        beginTime.set(Calendar.SECOND, 0);
        endTime.set(Calendar.HOUR, 18);
        endTime.set(Calendar.MINUTE, 0);
        endTime.set(Calendar.SECOND, 0);
        long startMillis = beginTime.getTimeInMillis();
        long endMillis = endTime.getTimeInMillis();

        ContentResolver cr = getContentResolver();
        // Introduce datos a contenedor que se inserta como un evento en el calendario
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, titulo);
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, tz.getID());

        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
    }

    public void insertarFecha() {
        if (this.isNetworkAvailable()) {
            task = new ConnectFecha();
            task.execute();

            int k = 0;
            while (k < fechas.length) {
                fechas[k] = "";
                ++k;
            }

            boolean continuar = false;

            while (!continuar) {
                continuar = task.continuar;
            }
            if (continuar) {
                this.fechas = task.getFechas();
                int i = 0;
                int dia;
                String titulo;
                int mes;
                while (fechas[i] != "") {
                    if (fechas[i] == null) {
                        dia = 1;
                    } else {
                        dia = Integer.parseInt(fechas[i]);
                    }
                    ++i;
                    if (fechas[i] == null) {
                        mes = 1;
                    } else {
                        mes = Integer.parseInt(fechas[i]) - 1;
                    }
                    ++i;
                    titulo = fechas[i];
                    ++i;
                    insertarFechasEnCalendario(dia, titulo, mes);
                }
            }
        }
        else{
            Toast.makeText(this, "NO HAY CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
        }
    }
}
