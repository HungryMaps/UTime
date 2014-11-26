/*
 * Autores: Jennifer Ledezma
 *          Ana Laura Berdasco
 * Clase NotaDetail: Mantiene la conexión entre las funciones propias de sql y el interfaz para
 *                   Agregar, Borrar o AActualizar una nota
 */

package com.example.android.utime.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class NotaDetail extends Activity {

    private EditText editTextNameNota;
    private EditText editTextComentarioNota;
    private int _Nota_Id = 0;
    String nombreUsuario;

    private Cursor note;
    private Nota NotesDbAdapter;
    private DBhelper mDbHelper;
    private SQLControlador sqlControlador;
    public static String curText = "";

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota_detail);

        editTextNameNota = (EditText) findViewById(R.id.editTextNameNota);
        editTextComentarioNota = (EditText) findViewById(R.id.editTextComentarioNota);
        _Nota_Id = 0;
        Intent intent = getIntent();
        _Nota_Id = intent.getIntExtra("nota_Id", 0);
        SQLControlador repo = new SQLControlador(this);
        Nota nota = new Nota();
        nota = repo.getNotaById(_Nota_Id);

        if (nota != null) {
            editTextNameNota.setText(nota.nameNota);
            editTextComentarioNota.setText(nota.comentarioNota);
        }

        //Para sacar el id del calendario
        String[] projection =
                new String[]{
                        CalendarContract.Calendars._ID,
                        CalendarContract.Calendars.NAME,
                        CalendarContract.Calendars.ACCOUNT_NAME,
                        CalendarContract.Calendars.ACCOUNT_TYPE};
        Cursor calCursor =
                getContentResolver().
                        query(CalendarContract.Calendars.CONTENT_URI,
                                projection,
                                CalendarContract.Calendars.VISIBLE + " = 1",
                                null,
                                CalendarContract.Calendars._ID + " ASC");
        if (calCursor.moveToFirst()) {
            do {
                long id = calCursor.getLong(0);
                String displayName = calCursor.getString(2);
                nombreUsuario = displayName;
            } while (calCursor.moveToNext());
        }
        System.out.println("nombreUsuario: " + nombreUsuario);
    }

    /**
     * Pequeña clase que dibuja las líneas para escribir tipo Notepad
     */
    public static class LineEditText extends EditText{

        private Rect mRect;
        private Paint mPaint;
        // we need this constructor for LayoutInflater
        public LineEditText(Context context, AttributeSet attrs) {
            super(context, attrs);
            mRect = new Rect();
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mPaint.setColor(Color.BLUE);
        }

        @Override
        protected void onDraw(Canvas canvas) {

            int height = getHeight();
            int line_height = getLineHeight();

            int count = height / line_height;

            if (getLineCount() > count)
                count = getLineCount();

            Rect r = mRect;
            Paint paint = mPaint;
            int baseline = getLineBounds(0, r);

            for (int i = 0; i < count; i++) {

                canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
                baseline += getLineHeight();

                super.onDraw(canvas);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(Nota.KEY_ID_NOTA, _Nota_Id);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*try {
            populateFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nota_detail, menu);
        return true;
    }

    /**
     * Método que se encarga de darle funcionalidad a cada icon del MenuBar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Caso que el usuario escoge about del Menu: Muestra información de la app
            case R.id.menu_about:

                AlertDialog.Builder dialog = new AlertDialog.Builder(NotaDetail.this);
                dialog.setTitle("About");
                dialog.setMessage("Universidad de Costa Rica\n" +
                                "Ingeniería del Software II\n\n" +
                                "Students: \n"+
                                "Ana Laura Berdasco, " +
                                "Jennifer Ledezma, " +
                                "Paula Lopez, " +
                                "Joan Marchena, " +
                                "David Ramirez\n\n" +
                                "UTime\n\n"
                                + "If there is any bug is found please freely e-mail us: " +
                                "\n\tutime@gmail.com"
                );
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                return true;

            //Usuario escoge el icon de: Papelera de Reciclaje en el MenuBar
            case R.id.btnDelete:
                deleteState();
                finish();
                return true;

            //Usuario escoge el icon de: Guardar en el MenuBar
            case R.id.btnSave:
                saveState();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Metodo que se encarga de hacer la funcionalidad de Guardar los cambios de una nota
     */
    private void saveState() {
        SQLControlador repo = new SQLControlador(this);
        Nota nota = new Nota();

        nota.comentarioNota = editTextComentarioNota.getText().toString();
        nota.nameNota = editTextNameNota.getText().toString();
        nota.nota_ID = _Nota_Id;

        if(_Nota_Id == 0){
            _Nota_Id = repo.insertNota(nota, nombreUsuario);
            Toast.makeText(this, "Has agregado una nota", Toast.LENGTH_SHORT).show();

        }else{
            repo.updateNota(nota, nombreUsuario);
            Toast.makeText(this, "Lista Actualizada", Toast.LENGTH_SHORT).show();
        }
        returnHome();  //Actualiza la informacion
    }
    /**
     * Metodo auxiliar que se encarga de hacer la funcionalidad de Borrar una nota
     */
    private void deleteState() {
        SQLControlador erase = new SQLControlador(this);
        erase.deleteNota(_Nota_Id, nombreUsuario);
        Toast.makeText(this, "Nota Eliminada", Toast.LENGTH_SHORT).show();
        returnHome(); // para que vuelva a la pagina de notas*/
    }

    /**
     * Metodo que permite volver a la página principal
     */
    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(),
                Notas.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }
}