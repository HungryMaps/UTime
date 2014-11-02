package com.example.android.utime.app;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Paula on 02/11/2014.
 */

/*
* Clase que se encarga de la conexión con la base de datos remota
 */
public class ConnectEvaluacion extends AsyncTask<String, Void, String> {
    EvaluacionPorCurso evaluacion;
    private static String databaseBaseURL = "jdbc:mysql://Paula.db.4676399.hostedresource.com:3306/Paula";
    public String user = "Paula";
    public String pass = "Lopez123#";
    private Connection conexion;

    public ConnectEvaluacion(EvaluacionPorCurso evaluacion) {
        this.evaluacion = evaluacion;
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
            conexion = DriverManager.getConnection(databaseBaseURL, user, pass);
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFound: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        if (conexion != null) {
            System.out.println("Database connection success");
            Statement statement = null;
            ResultSet result = null;
            try {
                statement = conexion.createStatement();
                String query = "INSERT INTO  `Paula`.`Evaluacion` (id, name, idCurso, calificacion, evaluacion) VALUES (" + evaluacion.evaluacion_ID + ", '" + evaluacion.name
                        + "', '" + evaluacion.curso_ID + "', '" + evaluacion.calificacion + "', '" + evaluacion.evaluacion + "');";
                statement.executeUpdate(query);
                System.out.println("Hice Query");

            } catch (SQLException ex) {
                // handle any errors
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException sqlEx) {
                } // ignore
                result = null;
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqlEx) {
                } // ignore
                statement = null;
            }
        }
        return "";
    }
}
