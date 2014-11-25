/*
 * Autor: Paula López
 * Clase Calendario: Muestra el calendario de la aplicación, además de un espacio para notas generales
 *                  que se quieran hacer.
 */


package com.example.android.utime.app;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
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

    private void setearSharedPreferences(Long id){
        SharedPreferences shared = getPreferences(MODE_PRIVATE);
        shared.edit().putLong("Calendario", id).commit();
    }

    public void preferencias(View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(Calendario.this);
        dialog.setTitle("Escoger Calendario");
        final ArrayList<String> lista = obtenerCalendarios();
        final StableArrayAdapter adapter = new StableArrayAdapter(this,android.R.layout.simple_list_item_1, lista);
        dialog.setAdapter(adapter, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList<Long> list = obtenerCalendariosID();
                setearSharedPreferences(list.get(which));
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    // Adapter usado para agregar a la lista, hereda de Array Adapter

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
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


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private ArrayList<String> obtenerCalendarios(){
        ArrayList<String> lista = new ArrayList<String>();
        String[] projection =
                new String[]{
                        Calendars.NAME
                };
        Cursor calCursor =
                getContentResolver().
                        query(Calendars.CONTENT_URI,
                                projection,
                                Calendars.VISIBLE + " = 1",
                                null,
                                Calendars._ID + " ASC");
        if (calCursor.moveToFirst()) {
            do {
                lista.add(calCursor.getString(0));
            } while (calCursor.moveToNext());
        }
        return lista;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private ArrayList<Long> obtenerCalendariosID(){
        ArrayList<Long> lista = new ArrayList<Long>();
        String[] projection =
                new String[]{
                        Calendars._ID
                };
        Cursor calCursor =
                getContentResolver().
                        query(Calendars.CONTENT_URI,
                                projection,
                                Calendars.VISIBLE + " = 1",
                                null,
                                Calendars._ID + " ASC");
        if (calCursor.moveToFirst()) {
            do {
                lista.add(calCursor.getLong(0));
            } while (calCursor.moveToNext());
        }
        return lista;
    }

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
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        long calID = pref.getLong("Calendario", 1);

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
