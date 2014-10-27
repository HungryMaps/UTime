package com.example.android.utime.app;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Paula on 26/10/2014.
 */


/**
 * Clase que usa AsyncTask para poder abrir la conexión con la base de datos
 */
public class ConnectNotas extends AsyncTask<String, Void, String> {

    Nota nota;
    private static String databaseBaseURL = "jdbc:mysql://Paula.db.4676399.hostedresource.com:3306/Paula";
    public String user = "Paula";
    public String pass = "Lopez123#";
    private Connection con;

    public ConnectNotas(Nota nota, int nota_Id){
        this.nota = nota;
        nota.nota_ID = nota_Id;
    }

    /*
    * Método que se encarga de abrir una conexión con la base de datos remota
    * y de insertar los datos de la nota en la tabla de Nota de ahí.
     */

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
                String query = "INSERT INTO  `Paula`.`Nota` (idNota, nameNota, comentarioNota) VALUES (" + nota.nota_ID + ", '"+nota.nameNota+"', '"+nota.comentarioNota+"');";
                stmt.executeUpdate(query);
                System.out.println("Hice Query");

            } catch (SQLException ex) {
                // handle any errors
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore
                stmt = null;
            }
        } catch (ClassNotFoundException e) {
        } catch (SQLException e) {
        }
        return "";
    }
}
