package com.example.android.utime.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CursoDetail extends ActionBarActivity implements android.view.View.OnClickListener {

    Button btnSave ,  btnDelete;

    EditText editTextName;
    EditText editTextProfesor;
    EditText editTextAula;
    EditText editTextDias;
    EditText editTextHoras;
    EditText editTextSemestre;
    EditText editTextAnno;
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


        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextProfesor = (EditText) findViewById(R.id.editTextProfesor);
        editTextAula = (EditText) findViewById(R.id.editTextAula);
        editTextDias = (EditText) findViewById(R.id.editTextDias);
        editTextHoras = (EditText) findViewById(R.id.editTextHoras);
        editTextSemestre = (EditText) findViewById(R.id.editTextSemestre);
        editTextAnno = (EditText) findViewById(R.id.editTextAnno);

        btnSave.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        _Curso_Id =0;
        Intent intent = getIntent();
        _Curso_Id =intent.getIntExtra("curso_Id", 0);
        SQLControlador repo = new SQLControlador(this);
        Curso curso = new Curso();
        curso = repo.getCursoById(_Curso_Id);

        editTextAula.setText(curso.aula);
        editTextName.setText(curso.name);
        editTextProfesor.setText(curso.profesor);
        editTextDias.setText(curso.dias);
        editTextHoras.setText(curso.horas);
        editTextSemestre.setText(curso.semestre);
        editTextAnno.setText(curso.anno);
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

    public void onClick(View view) {
        if (view == findViewById(R.id.btnSave)){
            SQLControlador repo = new SQLControlador(this);
            Curso curso = new Curso();
            curso.anno = editTextAnno.getText().toString();
            curso.semestre = editTextSemestre.getText().toString();
            curso.horas = editTextHoras.getText().toString();
            curso.dias = editTextDias.getText().toString();
            curso.aula= editTextAula.getText().toString();
            curso.profesor=editTextProfesor.getText().toString();
            curso.name=editTextName.getText().toString();
            curso.curso_ID=_Curso_Id;

            if (_Curso_Id==0) {
                _Curso_Id = repo.insert(curso);

                Toast.makeText(this,"Se agrego un nuevo curso",Toast.LENGTH_SHORT).show();

            }
                else{
                repo.update(curso);
                Toast.makeText(this,"Curso Actualizado",Toast.LENGTH_SHORT).show();
                }

           finish(); // para que vuelva a la pagina de cursos
        }

        else if (view== findViewById(R.id.btnDelete)){
            SQLControlador erase = new SQLControlador(this);
            erase.delete(_Curso_Id);
            Toast.makeText(this, "Curso Eliminado", Toast.LENGTH_SHORT);

            finish(); // para que vuelva a la pagina de cursos
        }


        /*Prevista para conexión con la base remota */

        Connect();

    }

    /*
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

    /*
    Método que llama a la conexión de la base
     */

    public void Connect() {
        Connect task = new Connect();
        task.execute();
    }

    public void returnHome() {

        Intent home_intent = new Intent(getApplicationContext(),
                Cursos.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(home_intent);

    }
}
