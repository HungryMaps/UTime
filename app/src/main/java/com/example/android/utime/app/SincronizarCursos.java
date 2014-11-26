package com.example.android.utime.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.text.format.Time;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Pau on 20/11/2014.
 */

/**
 * Clase que se encarga de conectarse con la base de datos externa y de traer los datos de los cursos del
 * usuario específico
 */
public class SincronizarCursos extends AsyncTask<String, Void, String> {
    private static String databaseBaseURL = "jdbc:mysql://Paula.db.4676399.hostedresource.com:3306/Paula";
    public String user = "Paula";
    public String pass = "Lopez123#";
    private Connection con;
    private String usuario;
    private Context contexto;
    public String sincronizacionNotas = "";
    ArrayList<HashMap<String, String>> cursoListSinc = new ArrayList<HashMap<String, String>>();
    private DBhelper dbHelper;
    HashMap<String, String> cursoSinc;
    String id, name, profesor, aula, dias, horas, semestre, anno = "";

    public SincronizarCursos(String usuario, Context context) {
        this.usuario = usuario;
        this.contexto = context;
    }

    /*
    * Método que se encarga de abrir una conexión con la base de datos remota
    * y de traer los datos de la base remota
    */
    @Override
    protected String doInBackground(String... strings) {
        try {
            //Driver para abrir la conexión con la base de mysql
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(databaseBaseURL, user, pass);
        } catch (ClassNotFoundException e) {
        } catch (SQLException e) {
        }
        if (con != null) {
            System.out.println("Database connection success");
            Statement statement = null;
            String result = "";
            try {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select * from Curso WHERE idUsuario = '" + usuario + "' ;");

                int i = 0;
                //Hacer la lista nueva
                dbHelper = new DBhelper(contexto);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("DELETE FROM CURSO");

                while (rs.next()) {
                    cursoSinc = new HashMap<String, String>();
                    id = rs.getString(2);
                    name = rs.getString(3);
                    profesor = rs.getString(4);
                    aula = rs.getString(5);
                    dias = rs.getString(6);
                    horas = rs.getString(7);

                    cursoSinc.put("id", id);
                    cursoSinc.put("name", name);

                    if (cursoListSinc.isEmpty()) {
                        cursoListSinc.add(cursoSinc);
                    } else {
                        if (!cursoListSinc.contains(cursoSinc)) {
                            cursoListSinc.add(cursoSinc);
                        }
                    }
                    result += rs.getString(1) + "|";
                    result += rs.getInt(2) + "|";
                    result += rs.getString(3) + "|";
                    result += rs.getString(4) + "|";
                    result += rs.getString(5) + "|";
                    result += rs.getString(6) + "|";
                    result += rs.getString(7) + "|";
                    result += "\n";
                    ++i;

                    Time now = new Time();
                    now.setToNow();
                    anno = Integer.toString(now.year);
                    if(now.month < 7){
                        semestre =  "I";
                    }else{
                        semestre = "II";
                    }

                    dbHelper = new DBhelper(contexto);
                    db = dbHelper.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put("horas", horas);
                    values.put("dias", dias);
                    values.put("aula", aula);
                    values.put("profesor", profesor);
                    values.put("name", name);
                    values.put("semestre", semestre);
                    values.put("anno", anno);

                    //db.insert("Curso", id, values);
                    long curso_Id = db.insert(Curso.TABLE, null, values);

                    // Insertando filas
                    db.close(); // Cerrando la connecion de la base de datos
                }
                result += "\n";
                System.out.println("Resultado Cursos: \n" + result);
                this.sincronizacionNotas = result;
            } catch (SQLException ex) {
                // handle any errors
                ex.printStackTrace();
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }
        }
        return "Fin";
    }
}
