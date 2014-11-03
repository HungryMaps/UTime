package com.example.android.utime.app;

import android.os.AsyncTask;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Pau on 02/11/2014.
 */
public class ConnectFecha extends AsyncTask<String, Void, String> {

    private static String databaseBaseURL = "jdbc:mysql://Paula.db.4676399.hostedresource.com:3306/Paula";
    public String user = "Paula";
    public String pass = "Lopez123#";
    private Connection con;

    /*
    * Constructor, con la nota, su id y el id del device
     */
    public ConnectFecha() {

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
        } catch (ClassNotFoundException e) {
        } catch (SQLException e) {
        }
        if (con != null) {
            System.out.println("Database connection success");
            Statement statement = null;
            String result = "";
            try {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select * from Fecha");
                ResultSetMetaData rsmd = rs.getMetaData();

                while (rs.next()) {
                    result += rsmd.getColumnName(1) + ": " + rs.getInt(1) + "\n";
                    result += rsmd.getColumnName(2) + ": " + rs.getString(2) + "\n";
                    result += rsmd.getColumnName(3) + ": " + rs.getString(3) + "\n";
                }
                System.out.println("Resultado: \n" + result);

            } catch (SQLException ex) {
                // handle any errors
                ex.printStackTrace();
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }
        }

        return "";
    }
}
