package com.example.android.utime.app;

import android.os.AsyncTask;
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
    private Calendario calendario;
    public String[] fechas = new String[300];
    public boolean continuar = false;

    /*
    * Constructor vacío de momento
     */
    public ConnectFecha() {
        calendario = new Calendario();
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
                ResultSet rs = st.executeQuery("select * from Fecha");
                ResultSetMetaData rsmd = rs.getMetaData();

                int indice = 0;

                while (rs.next()) {
                    result += rsmd.getColumnName(1) + ": " + rs.getInt(1) + "\n";
                    result += rsmd.getColumnName(2) + ": " + rs.getString(2) + "\n";
                    result += rsmd.getColumnName(3) + ": " + rs.getString(3) + "\n";
                    // hay que insertar estos datos en el calendario como eventos
                    fechas[indice] = rs.getInt(1) + "";
                    ++indice;
                    fechas[indice] = rs.getInt(3) + "";
                    ++indice;
                    fechas[indice] = rs.getString(2) + "";
                    ++indice;
                }
                while (indice < fechas.length) {
                    fechas[indice] = "";
                    ++indice;
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
        cambiarContinuar();
        return "";
    }
    public String[] getFechas() {
        return fechas;
    }

    public void setFechas(String[] fechas) {
        this.fechas = fechas;
    }

    public boolean cambiarContinuar() {
        return continuar = true;
    }
}
