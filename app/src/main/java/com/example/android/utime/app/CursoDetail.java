/*
 * Autores: Jennifer Ledezma
 *          Ana Laura Berdasco
 * Clase CursoDetail: Mantiene la conexión entre las funciones propias de sql y el interfaz para
 *                   Agregar, Borrar o AActualizar un curso
 */

package com.example.android.utime.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;

public class CursoDetail extends Activity {

    private EditText editTextName;
    private EditText editTextProfesor;
    private EditText [] editTextAula = new EditText[5];

    private int _Curso_Id=0;
    String nombreUsuario;

    private Cursor course;
    private Curso CursoDbAdapter;
    private DBhelper mDbHelper;
    private SQLControlador sqlControlador;
    public static String curText = "";

    // Variable Global para ver cuantos spinners hay visibles
    private int contadorSpinners = 0;
    // Variables para guardar los combobox que contienen los posibles días y horas
    // Así como los posibles valores
    private Spinner[][] spinners = new Spinner[5][3];
    private String array_spinner[];
    private String horasi_spinner[];
    private String horasf_spinner[];

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curso_detail);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextProfesor = (EditText) findViewById(R.id.editTextProfesor);

        for (int k = 0; k < 5; k++) {
            for (int i = 0; i < 3; i++) {
                String spinner = "spinner" + Integer.toString(k) + Integer.toString(i);
                int id = getResources().getIdentifier(spinner, "id", getPackageName());
                spinners[k][i] = (Spinner) findViewById(id);
            }
        }

        for (int k = 0; k < 5; k++) {
            String campo = "editTextAula" + Integer.toString(k);
            int id = getResources().getIdentifier(campo, "id", getPackageName());
            editTextAula[k] = (EditText) findViewById(id);
        }

        _Curso_Id =0;
        Intent intent = getIntent();
        _Curso_Id =intent.getIntExtra("curso_Id", 0);
        SQLControlador repo = new SQLControlador(this);
        Curso curso = new Curso();
        curso = repo.getCursoById(_Curso_Id);

        array_spinner=new String[6];
        array_spinner[0]="Lunes";
        array_spinner[1]="Martes";
        array_spinner[2]="Miercoles";
        array_spinner[3]="Jueves";
        array_spinner[4]="Viernes";
        array_spinner[5]="Sabado";
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array_spinner);

        for(int i=0; i<5; i++){
            spinners[i][0].setAdapter(adapter);
        }

        horasi_spinner=new String[13];
        horasi_spinner[0]="7";
        horasi_spinner[1]="8";
        horasi_spinner[2]="9";
        horasi_spinner[3]="10";
        horasi_spinner[4]="11";
        horasi_spinner[5]="12";
        horasi_spinner[6]="13";
        horasi_spinner[7]="14";
        horasi_spinner[8]="15";
        horasi_spinner[9]="16";
        horasi_spinner[10]="17";
        horasi_spinner[11]="19";
        horasi_spinner[12]="20";
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, horasi_spinner);
        for(int i=0; i<5; i++){
            spinners[i][1].setAdapter(adapter);
        }

        horasf_spinner=new String[15];
        horasf_spinner[0]="7:50";
        horasf_spinner[1]="8:50";
        horasf_spinner[2]="9:50";
        horasf_spinner[3]="10:50";
        horasf_spinner[4]="11:50";
        horasf_spinner[5]="12:50";
        horasf_spinner[6]="13:50";
        horasf_spinner[7]="14:50";
        horasf_spinner[8]="15:50";
        horasf_spinner[9]="16:50";
        horasf_spinner[10]="17:50";
        horasf_spinner[11]="18:50";
        horasf_spinner[12]="19:50";
        horasf_spinner[13]="20:50";
        horasf_spinner[14]="21:50";
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, horasf_spinner);
        for(int i=0; i<5; i++){
            spinners[i][2].setAdapter(adapter);
        }

        if(curso != null) {

            editTextName.setText(curso.name);
            editTextProfesor.setText(curso.profesor);
            String[] dias = curso.dias.split(",");
            String[] horas = curso.horas.split(",");
            String[] editAula = curso.aula.split(",");

            for (int i = 0; i < dias.length; i++) {

                editTextAula[i].setText(editAula[i]);
                editTextAula[i].setVisibility(View.VISIBLE);

                int posicion = 0;

                if (dias[i].equals("Lunes")) {
                    posicion = 0;
                }
                if (dias[i].equals("Martes")) {
                    posicion = 1;
                }
                if (dias[i].equals("Miercoles")) {
                    posicion = 2;
                }
                if (dias[i].equals("Jueves")) {
                    posicion = 3;
                }
                if (dias[i].equals("Viernes")) {
                    posicion = 4;
                }
                if (dias[i].equals("Sabado")) {
                    posicion = 5;
                }

                contadorSpinners++;
                spinners[i][0].setVisibility(View.VISIBLE);
                spinners[i][0].setSelection(posicion);

                int fila = 0;

                switch (Integer.parseInt(horas[2 * i])) {
                    case 7:
                        fila = 0;
                        break;
                    case 8:
                        fila = 1;
                        break;
                    case 9:
                        fila = 2;
                        break;
                    case 10:
                        fila = 3;
                        break;
                    case 11:
                        fila = 4;
                        break;
                    case 12:
                        fila = 5;
                        break;
                    case 13:
                        fila = 6;
                        break;
                    case 14:
                        fila = 7;
                        break;
                    case 15:
                        fila = 8;
                        break;
                    case 16:
                        fila = 9;
                        break;
                    case 17:
                        fila = 10;
                        break;
                    case 18:
                        fila = 11;
                        break;
                    case 19:
                        fila = 12;
                        break;
                    case 20:
                        fila = 13;
                        break;
                    case 21:
                        fila = 14;
                        break;
                    default:
                        break;
                }

                int fila2 = 0;
                String[] temp = horas[2 * i + 1].split(":");
                switch (Integer.parseInt(temp[0])) {
                    case 7:
                        fila2 = 0;
                        break;
                    case 8:
                        fila2 = 1;
                        break;
                    case 9:
                        fila2 = 2;
                        break;
                    case 10:
                        fila2 = 3;
                        break;
                    case 11:
                        fila2 = 4;
                        break;
                    case 12:
                        fila2 = 5;
                        break;
                    case 13:
                        fila2 = 6;
                        break;
                    case 14:
                        fila2 = 7;
                        break;
                    case 15:
                        fila2 = 8;
                        break;
                    case 16:
                        fila2 = 9;
                        break;
                    case 17:
                        fila2 = 10;
                        break;
                    case 18:
                        fila2 = 11;
                        break;
                    case 19:
                        fila2 = 12;
                        break;
                    case 20:
                        fila2 = 13;
                        break;
                    case 21:
                        fila2 = 14;
                        break;
                    default:
                        break;
                }

                spinners[i][1].setVisibility(View.VISIBLE);
                spinners[i][1].setSelection(fila);
                spinners[i][2].setVisibility(View.VISIBLE);
                spinners[i][2].setSelection(fila2);
            }
        }else{
            contadorSpinners++;
        }

        // Para sacar el id del calendario
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.curso_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            //Usuario escoge abrir Evaluacion relacionada con el curso, entonces inicia la activity de esto
            case R.id.btnEvaluar:
                _Curso_Id =0;
                Intent intent = getIntent();
                _Curso_Id =intent.getIntExtra("curso_Id", 0);
                SQLControlador repo = new SQLControlador(this);
                Curso curso = new Curso();
                curso = repo.getCursoById(_Curso_Id);

                //aquí le decimos de donde vamos (la ventana donde estoy) y hacia donde voy
                Intent in = new Intent(CursoDetail.this, Evaluacion.class);
                // Envía parámetro de id del curso
                in.putExtra("curso_ID", curso.curso_ID);
                startActivity(in);
                return true;

            //Usuario escoge abrir archivos relacionados con el curso, entonces inicia la activity de esto
            case R.id.btnArchivo:
                _Curso_Id =0;
                Intent intent2 = getIntent();
                _Curso_Id =intent2.getIntExtra("curso_Id", 0);
                SQLControlador repo2 = new SQLControlador(this);
                Curso curso2 = new Curso();
                curso = repo2.getCursoById(_Curso_Id);

                //aquí le decimos de donde vamos (la ventana donde estoy) y hacia donde voy
                Intent in2 = new Intent(CursoDetail.this, Archivos.class);
                // Envía parámetro de id del curso
                in2.putExtra("curso_name", curso.name);
                startActivity(in2);
                return true;

            //Caso que el usuario escoge about del Menu: Muestra información de la app
            case R.id.menu_about:

                AlertDialog.Builder dialog = new AlertDialog.Builder(CursoDetail.this);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Metodo que se encarga de hacer la funcionalidad de Guardar los cambios de una curso
     */
    private void saveState() {
        SQLControlador repo = new SQLControlador(this);
        Curso curso = new Curso();
        curso.dias = "";
        curso.horas = "";
        curso.aula = "";
        for(int i=0; i<contadorSpinners; i++){
            curso.dias += spinners[i][0].getSelectedItem().toString() + ",";
            curso.horas +=  spinners[i][1].getSelectedItem().toString() + "," + spinners[i][2].getSelectedItem().toString() + ",";
            curso.aula += editTextAula[i].getText().toString() + ",";
        }

        Time now = new Time();
        now.setToNow();
        curso.anno = Integer.toString(now.year);
        if(now.month < 7){
            curso.semestre = "I";
        }else{
            curso.semestre = "II";
        }
        curso.profesor=editTextProfesor.getText().toString();
        curso.name=editTextName.getText().toString();
        curso.curso_ID=_Curso_Id;

        if (_Curso_Id==0) {
            _Curso_Id = repo.insert(curso, nombreUsuario);
            crearCarpeta(curso.name);
            Toast.makeText(this,"Se agrego un nuevo Curso",Toast.LENGTH_SHORT).show();

        }
        else{
            repo.update(curso, nombreUsuario);
            Toast.makeText(this,"Curso Actualizado",Toast.LENGTH_SHORT).show();
        }

        returnHome();

    }

    /* Método que se encarga de borrar el curso de la base de datos */
    private void deleteState() {
        borrarCarpeta();
        SQLControlador erase = new SQLControlador(this);
        erase.delete(_Curso_Id, nombreUsuario);
        Toast.makeText(this, "Curso Eliminado", Toast.LENGTH_SHORT).show();
        returnHome(); // para que vuelva a la pagina de cursos*/
    }

    /* Método que se encarga de borrar la carpeta relacionada al curso */
    private void borrarCarpeta(){
        Intent intent = getIntent();
        int id = intent.getIntExtra("curso_Id", 0);
        SQLControlador repo = new SQLControlador(this);
        Curso cur = repo.getCursoById(id);
        if(isExternalStorageWritable()){
            File sdDir = Environment.getExternalStorageDirectory();
            String path = sdDir.getPath()+"/UTimeFiles/"+cur.name;
            File file = new File(path);
            file.delete();
        }
    }

    /* Crea la carpeta para este curso dentro del directorio UTime */
    private void crearCarpeta(String nombre){
        if(isExternalStorageWritable()){
            File sdDir = Environment.getExternalStorageDirectory();
            String path = sdDir.getPath()+"/UTimeFiles/"+nombre;
            File file = new File(path);
            file.mkdir();
        }
    }

    /* Revisa que el almacenamiento externo esté disponible */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Me permite volver a la pagina principal
     */
    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(),
                Cursos.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }

    /**
     * Método Para reaccionar ante la inclusión de otro posible día para ese curso
     * Ubica las posibles combobox y vuelve visible una más
     * @param view
     */
    public void Agregar(View view){
        if(contadorSpinners < 5) {
            for(int i=0;i<3;i++) {
                String spinner = "spinner" + Integer.toString(contadorSpinners) + Integer.toString(i);
                int id = getResources().getIdentifier(spinner, "id", getPackageName());
                Spinner local = (Spinner) findViewById(id);
                local.setVisibility(View.VISIBLE);
            }

            String editText = "editTextAula" + Integer.toString(contadorSpinners);
            int id2 = getResources().getIdentifier(editText, "id", getPackageName());
            EditText temp = (EditText) findViewById(id2);
            temp.setVisibility(View.VISIBLE);

            contadorSpinners++;
        }else{
            Toast.makeText(this, "Número de días máximo", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Método para reaccionar ante la reducción de la cantidad de días disponibles
     * Ubica las posibles combobox y vuelve invisible una más
     * @param view
     */
    public void Quitar(View view){
        if(contadorSpinners > 1) {
            for(int i=0;i<3;i++) {
                String spinner = "spinner" + Integer.toString(contadorSpinners-1) + Integer.toString(i);
                int id = getResources().getIdentifier(spinner, "id", getPackageName());
                Spinner local = (Spinner) findViewById(id);
                local.setVisibility(View.INVISIBLE);
            }

            String editText = "editTextAula" + Integer.toString(contadorSpinners-1);
            int id2 = getResources().getIdentifier(editText, "id", getPackageName());
            EditText temp = (EditText) findViewById(id2);
            temp.setVisibility(View.INVISIBLE);

            contadorSpinners--;
        }else{
            Toast.makeText(this, "Número de días mínimo", Toast.LENGTH_LONG).show();
        }
    }
}