package com.example.android.utime.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CursoDetail extends ActionBarActivity implements android.view.View.OnClickListener {

    // Variable Global para ver cuantos spinners hay visibles
    int contadorSpinners = 0;

    Button btnSave ,  btnDelete;

    EditText editTextName;
    EditText editTextProfesor;
    EditText editTextAula;

    // Variables para guardar los combobox que contienen los posibles días y horas
    // Así como los poisbles valores
    Spinner[][] spinners = new Spinner[5][3];
    private String array_spinner[];
    private String horasi_spinner[];
    private String horasf_spinner[];

    private int _Curso_Id=0;

    private static String databaseBaseURL = "jdbc:mysql://Paula.db.4676399.hostedresource.com:3306/Paula";
    public String user = "Paula";
    public String pass = "Lopez123#";
    private Connection con;
    String resultado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curso_detail);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        Button btnEvaluacion = (Button) findViewById(R.id.btnEvaluacion);

        btnEvaluacion.setOnClickListener(new View.OnClickListener(){

            /**
             * REQ: que se haya capturado bien el botón al que se le hizo click
             * @param view
             * EFE: Carga en tiempo de ejecución una nueva vista o actividad según corresponda
             */
            @Override
            public void onClick(View view){
                //aquí le decimos de donde vamos (la ventana donde estoy) y hacia donde voy
                Intent in = new Intent(CursoDetail.this, Evaluacion.class);
                //lanza la siguiente ventana
                startActivity(in);
            }
        });

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextProfesor = (EditText) findViewById(R.id.editTextProfesor);
        editTextAula = (EditText) findViewById(R.id.editTextAula);

        for(int k=0; k<5; k++){
            for(int i=0; i<3; i++){
                String spinner = "spinner"+Integer.toString(k)+Integer.toString(i);
                int id = getResources().getIdentifier(spinner, "id", getPackageName());
                spinners[k][i] = (Spinner) findViewById(id);
            }
        }

        btnSave.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

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
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, array_spinner);
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

            editTextAula.setText(curso.aula);
            editTextName.setText(curso.name);
            editTextProfesor.setText(curso.profesor);
            String[] dias = curso.dias.split(",");
            String[] horas = curso.horas.split(",");

            for (int i = 0; i < dias.length; i++) {

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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Al hacer click en los botones me permite completar una accion
     * @param view
     */
    public void onClick(View view) {
        if (view == findViewById(R.id.btnSave)){
            SQLControlador repo = new SQLControlador(this);
            Curso curso = new Curso();
            curso.dias = "";
            curso.horas = "";
            for(int i=0; i<contadorSpinners; i++){
                    curso.dias += spinners[i][0].getSelectedItem().toString() + ",";
                    curso.horas +=  spinners[i][1].getSelectedItem().toString() + "," + spinners[i][2].getSelectedItem().toString() + ",";
            }

            Time now = new Time();
            now.setToNow();
            curso.anno = Integer.toString(now.year);
            if(now.month < 7){
                curso.semestre = "I";
            }else{
                curso.semestre = "II";
            }
            curso.aula= editTextAula.getText().toString();
            curso.profesor=editTextProfesor.getText().toString();
            curso.name=editTextName.getText().toString();
            curso.curso_ID=_Curso_Id;

            if (_Curso_Id==0) {
                _Curso_Id = repo.insert(curso);

                Toast.makeText(this,"Se agrego un nuevo Curso",Toast.LENGTH_SHORT).show();

            }
                else{
                repo.update(curso);
                Toast.makeText(this,"Curso Actualizado",Toast.LENGTH_SHORT).show();
                }

            returnHome();// para que vuelva a la pagina de cursos
        }

        else if (view== findViewById(R.id.btnDelete)){
            SQLControlador erase = new SQLControlador(this);
            erase.delete(_Curso_Id);
            Toast.makeText(this, "Curso Eliminado", Toast.LENGTH_SHORT);
            returnHome(); // para que vuelva a la pagina de cursos

        }


        /*Prevista para conexión con la base remota */

        Connect();

    }

    /**
     * Clase que usa AsyncTask para poder abrir la conexión con la base de datos
     */
    private class Connect extends AsyncTask <String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                //Driver para abrir la conexión con la base de mysql
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(databaseBaseURL, user, pass);
                System.out.println("Database connection success");

                Statement stmt = null;
                ResultSet rs = null;

                try {
                    stmt = con.createStatement();
                    rs = stmt.executeQuery("SELECT * FROM Curso WHERE id = 1");
                    System.out.println("Hice Query");

                    // Un ejemplo sobre cómo traer datos de la base

                    while(rs.next()){
                        resultado = resultado + rs.getString("id") + "&" + rs.getString("name") + "&"+ rs.getString("profesor") + "&"+ rs.getString("aula");
                        //Here "&"s are added to the return string. This is help to split the string in Android application
                    }
                    System.out.println("Resultado: " + resultado);
                } catch (SQLException ex){
                    // handle any errors
                    System.out.println("SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState());
                    System.out.println("VendorError: " + ex.getErrorCode());
                }

                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException sqlEx) { } // ignore
                    rs = null;
                }
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException sqlEx) { } // ignore
                    stmt = null;
                }
            }

            catch (ClassNotFoundException e) {
                int k=0;
                Toast.makeText(getApplicationContext(), "Error1: " + e.getMessage(),Toast.LENGTH_LONG).show();
            } catch (SQLException e) {
                int k=0;
                Toast.makeText(getApplicationContext(), "Error2: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return "";
        }
    }

    /**
     * Método que llama a la conexión de la base
     */
    public void Connect() {
        Connect task = new Connect();
        task.execute();
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
            contadorSpinners--;
        }else{
            Toast.makeText(this, "Número de días mínimo", Toast.LENGTH_LONG).show();
        }
    }
}
