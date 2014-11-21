package com.example.android.utime.app;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Pau on 20/11/2014.
 */

/**
 * Clase que se encarga de eliminar un curso de la base Remota
 */
public class EliminarCurso extends AsyncTask<String, Void, String> {
    long curso;
    private static String databaseBaseURL = "jdbc:mysql://Paula.db.4676399.hostedresource.com:3306/Paula";
    public String user = "Paula";
    public String pass = "Lopez123#";
    private Connection con;
    private String usuario = "";

    public EliminarCurso(long curso, String usuario) {
        this.curso = curso;
        this.usuario = usuario;
    }

    /*
    * Método que se encarga de abrir una conexión con la base de datos remota y eliminar el curso
    * seleccionado de la base remota
    */
    @Override
    protected String doInBackground(String... strings) {
        try {
            //Driver para abrir la conexión con la base de mysql
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(databaseBaseURL, user, pass);
        } catch (ClassNotFoundException e) {
            return "Class not found";
        } catch (SQLException e) {
            return "SQL Exception";
        }
        if (con != null) {
            System.out.println("Database connection success");
            Statement statement = null;
            ResultSet result = null;
            try {
                statement = con.createStatement();
                String query = "DELETE FROM `Paula`.`Curso` WHERE ( idUsuario = '" + usuario + "' AND id = " + curso + ");";
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
