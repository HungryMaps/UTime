/*
 * Autores: Jennifer Ledezma
 *          Ana Laura Berdasco
 * Clase NotaDetail: Mantiene la conexión entre las funciones propias de sql y la interfaz
 */

package com.example.android.utime.app;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NotaDetail extends ActionBarActivity implements android.view.View.OnClickListener {

    Button btnSave;
    Button btnDelete;
    EditText editTextNameNota;
    EditText editTextComentarioNota;
    private int _Nota_Id = 0;
    String nombreUsuario;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota_detail);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        editTextNameNota = (EditText) findViewById(R.id.editTextNameNota);
       // editTextComentarioNota = (EditText) findViewById(R.id.editTextComentarioNota);

       // btnSave.setOnClickListener(this);
       // btnDelete.setOnClickListener(this);

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
                Toast.makeText(this, "Calendar " + displayName + " " + id, Toast.LENGTH_SHORT).show();
                nombreUsuario = displayName;
            } while (calCursor.moveToNext());
        }
        System.out.println("nombreUsuario: " + nombreUsuario);
    }

    public static class LineEditText extends EditText{
        // we need this constructor for LayoutInflater
        public LineEditText(Context context, AttributeSet attrs) {
            super(context, attrs);
            mRect = new Rect();
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mPaint.setColor(Color.BLUE);
        }

        private Rect mRect;
        private Paint mPaint;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nota_detail, menu);
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

    /**
     * Al hacer click en los botones me permite completar una accion
     *
     * @param view
     */
    public void onClick(View view) {
        if (view == findViewById(R.id.btnSave)) {
            SQLControlador repo = new SQLControlador(this);
            Nota nota = new Nota();

            nota.comentarioNota = editTextComentarioNota.getText().toString();
            nota.nameNota = editTextNameNota.getText().toString();
            nota.nota_ID = _Nota_Id;

            if (_Nota_Id == 0) {
                _Nota_Id = repo.insertNota(nota, nombreUsuario);
                Toast.makeText(this, "Has agregado una nota", Toast.LENGTH_SHORT).show();
            } else {
                repo.updateNota(nota, nombreUsuario);
                Toast.makeText(this, "Nota Actualizada", Toast.LENGTH_SHORT).show();
            }
            returnHome();// para que vuelva a la pagina de notas
        } else if (view == findViewById(R.id.btnDelete)) {
            SQLControlador erase = new SQLControlador(this);
            erase.deleteNota(_Nota_Id);
            Toast.makeText(this, "Nota Eliminada", Toast.LENGTH_SHORT);
            returnHome(); // para que vuelva a la pagina de notas
        }
    }

    /**
     * Me permite volver a la página principal
     */
    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(),
                Notas.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }

}