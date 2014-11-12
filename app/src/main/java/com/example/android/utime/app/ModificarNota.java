package com.example.android.utime.app;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Pau on 11/11/2014.
 */
public class ModificarNota extends AsyncTask<String, Void, String> {

    Nota nota;
    private static String databaseBaseURL = "jdbc:mysql://Paula.db.4676399.hostedresource.com:3306/Paula";
    public String user = "Paula";
    public String pass = "Lopez123#";
    private Connection con;
    private String usuario = "";

    /*
    * Constructor, con la nota, su id y el id del device
     */
    public ModificarNota(Nota nota, String usuario){
        this.nota = nota;
        this.usuario = usuario;
    }

    /*
    * Método que se encarga de abrir una conexión con la base de datos remota
    * y de modificar los datos de la nota en la tabla de Nota de ahí.
    */
    @Override
    protected String doInBackground(String... strings) {
        try {
            //Driver para abrir la conexión con la base de mysql
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(databaseBaseURL, user, pass);
        }
        catch(ClassNotFoundException e){
        }catch(SQLException e){
        }
        if (con != null) {
            System.out.println("Database connection success");
            Statement statement = null;
            ResultSet result= null;
            try {
                statement = con.createStatement();
                String query = "UPDATE `Paula`.`Nota` SET nameNota = '" + nota.nameNota + "', comentarioNota = '" + nota.comentarioNota + "' WHERE idUsuario = '" + usuario + "' AND idNota = '" + nota.nota_ID + "';";
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
