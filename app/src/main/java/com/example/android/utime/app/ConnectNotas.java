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
    private String usuario = "";

    /*
    * Constructor, con la nota, su id y el id del device
     */
    public ConnectNotas(Nota nota, int nota_Id, String usuario){
        this.nota = nota;
        nota.nota_ID = nota_Id;
        this.usuario = usuario;
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
                    String query = "INSERT INTO  `Paula`.`Nota` (idUsuario, idNota, nameNota, comentarioNota) VALUES ('" + usuario+ "', " + nota.nota_ID + ", '" + nota.nameNota + "', '" + nota.comentarioNota + "');";
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

