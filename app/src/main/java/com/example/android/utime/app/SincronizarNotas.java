package com.example.android.utime.app;

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
    HashMap<String, String> notaSinc = new HashMap<String, String>();
    ArrayList<HashMap<String, String>> notaListSinc = new ArrayList<HashMap<String, String>>();

    public SincronizarNotas(String usuario) {
        this.usuario = usuario;
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

                //Hacer la lista nueva
                while (rs.next()) {
                    notaSinc.put("idNota", rs.getString(2));
                    notaSinc.put("nameNota", rs.getString(3));
                    notaListSinc.add(notaSinc);
                    result += rs.getString(3) + "|";
                    result += rs.getString(4) + "|";
                    result += "\n";
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
