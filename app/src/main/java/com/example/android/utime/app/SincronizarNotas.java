package com.example.android.utime.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

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
public class SincronizarNotas extends AsyncTask<String, Void, String> {
    private static String databaseBaseURL = "jdbc:mysql://Paula.db.4676399.hostedresource.com:3306/Paula";
    public String user = "Paula";
    public String pass = "Lopez123#";
    private Connection con;
    private String usuario;
    public String sincronizacionNotas = "";
    public Boolean sinc;
    HashMap<String, String> notaSinc;
    ArrayList<HashMap<String, String>> notaListSinc = new ArrayList<HashMap<String, String>>();
    String id = "";
    String name = "";
    String comentarioNota="";
    private DBhelper dbHelper;
    public Boolean sincronizando;
    private Context contexto;

    public SincronizarNotas(String usuario, Context context) {
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
            //Revisar si hay usuario
            if (usuario.equals("")) {
                sinc = false;
            } else {
                sinc = true;
            }
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
                ResultSet rs = st.executeQuery("select * from Nota WHERE idUsuario = '" + usuario + "' ;");

                int i = 0;
                //Hacer la lista nueva
                dbHelper = new DBhelper(contexto);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("DELETE FROM NOTA");

                while (rs.next()) {
                    notaSinc = new HashMap<String, String>();
                    id = rs.getString(2);
                    name = rs.getString(3);
                    comentarioNota = rs.getString(4);
                    //notaSinc.put("idNota", rs.getString(2));
                    //notaSinc.put("nameNota", rs.getString(3));
                    notaSinc.put("idNota", id);
                    notaSinc.put("nameNota", name);
                    if (notaListSinc.isEmpty()) {
                        notaListSinc.add(notaSinc);
                    } else {
                        if(!notaListSinc.contains(notaSinc)) {
                            notaListSinc.add(notaSinc);
                        }
                    }
                    result += rs.getString(3) + "|";
                    result += rs.getString(4) + "|";
                    result += "\n";
                    ++i;

                    dbHelper = new DBhelper(contexto);
                    db = dbHelper.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put("comentarioNota", comentarioNota);
                    values.put("nameNota", name);
                    //values.put("idNota", id);

                    db.insert("Nota", id, values);

                    // Insertando filas
                    //int nota_Id = (int) db.insert(Nota.TABLE, null, values);
                    db.close(); // Cerrando la connecion de la base de datos

                }
                result += "\n";
                System.out.println("Resultado Notas: \n" + result);
                this.sincronizacionNotas = result;
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }
        }
        return "Fin";
    }
}
