/*
 * Autor: Paula López
 * Clase Calendario: Muestra el calendario de la aplicación, además de un espacio para notas generales
 *                  que se quieran hacer.
 */


package com.example.android.utime.app;


import android.annotation.TargetApi;
import android.content.ContentUris;
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
import java.util.GregorianCalendar;


public class Calendario extends ActionBarActivity {

    /*
    *  Método para crear el activity cuando lo llama menú.
    */
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

}
