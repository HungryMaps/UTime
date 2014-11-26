/*
 * Autores: Jennifer Ledezma
 *          Ana Laura Berdasco
 * Clase Cursos: Muestra los cursos guardados en la app por el usuario, y opción de interactuar
 *               de distintas formas con estos (crear un nuevo curso, editar, eliminar)
 */

package com.example.android.utime.app;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Cursos extends ListActivity {

    TextView curso_Id;
    Context cont;
    String exito = "";
    ArrayList<HashMap<String, String>> cursoList;
    boolean terminaSinc= false;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);

        SQLControlador repo = new SQLControlador(this);

        cursoList = repo.getCursoList();
        if (cursoList.size() != 0) {
            ListView lv = getListView();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    curso_Id = (TextView) view.findViewById(R.id.curso_Id);
                    String cursoId = curso_Id.getText().toString();
                    Intent objIndent = new Intent(getApplicationContext(), CursoDetail.class);
                    objIndent.putExtra("curso_Id", Integer.parseInt(cursoId));
                    startActivity(objIndent);
                }
            });
            ListAdapter adapter = new SimpleAdapter(Cursos.this, cursoList, R.layout.view_curso_entry, new String[]{"id", "name"}, new int[]{R.id.curso_Id, R.id.curso_name});
            setListAdapter(adapter);
        } else {
            Toast.makeText(this, "No has ingresado cursos!", Toast.LENGTH_SHORT).show();
        }
        cont = this;
    }

    /**
     * Evento para mostrar una actiividad
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cursos, menu);
        return true;
    }

    /**
     * Permite al usuario escoger algun item del MenuBar
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                AlertDialog.Builder dialog = new AlertDialog.Builder(Cursos.this);
                dialog.setTitle("About");
                dialog.setMessage("Universidad de Costa Rica\n" +
                                "Ingeniería del Software II\n\n" +
                                "Students: \n" +
                                "Ana Laura Berdasco, " +
                                "Jennifer Ledezma, " +
                                "Paula Lopez, " +
                                "Joan Marchena, " +
                                "David Ramirez\n\n" +
                                "UTime\n\n"
                                + "If there is any bug is found please freely e-mail us: " +
                                "\n\tutime@gmail.com"
                );
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                return true;

            /**
             * Botón para sincronizar las notas
             */
            case R.id.menu_sincronizar:
                //En el caso que la persona haga click en la opción de Sincronizar
                AlertDialog.Builder dialogS = new AlertDialog.Builder(Cursos.this);
                dialogS.setTitle("Sincronizar");
                dialogS.setMessage("¿Desea sincronizar sus datos con los datos que se" +
                                "encuentran en la base de datos externa?\n" +
                                "Tome en cuenta que los datos que insertó sin tener conexión " +
                                "a Internet, no se van a sincronizar.\n" +
                                "Esto puede tardar varios segundos..."
                );
                dialogS.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Cuando haga click en OK se van a sincronizar los datos
                        String nombreUsuario = "";
                        // Para sacar el id del calendario
                        String[] projection =
                                new String[]{
                                        CalendarContract.Calendars._ID,
                                        CalendarContract.Calendars.NAME,
                                        CalendarContract.Calendars.ACCOUNT_NAME,
                                        CalendarContract.Calendars.ACCOUNT_TYPE};
                        Cursor calCursor =
                                getContentResolver().
                                        query(CalendarContract.Calendars.CONTENT_URI,
                                                projection,
                                                CalendarContract.Calendars.VISIBLE + " = 1",
                                                null,
                                                CalendarContract.Calendars._ID + " ASC");
                        if (calCursor.moveToFirst()) {
                            do {
                                long id = calCursor.getLong(0);
                                String displayName = calCursor.getString(2);
                                nombreUsuario = displayName;
                            } while (calCursor.moveToNext());
                        }
                        System.out.println("nombreUsuario: " + nombreUsuario);

                        //Revisar si hay usuario y si hay conexión a internet
                        if (!nombreUsuario.equals("") && isNetworkAvailable()) {
                            SincronizarCursos(nombreUsuario, cont);
                            dialog.cancel();
                            exito = "Sincronizado!";
                        } else {
                            //Si no hay internet no se hace nada
                            if (!isNetworkAvailable()) {
                                exito = "NO HAY CONEXIÓN A INTERNET.";
                                dialog.cancel();
                            }
                            //Si no hay usuario, no se hace nada
                            if (nombreUsuario.equals("")) {
                                dialog.cancel();
                                exito = "Debe de ingresar un usuario para poder sincronizar.";
                            }
                        }
                        Toast.makeText(Cursos.this, exito, Toast.LENGTH_LONG).show();
                    }
                });
                dialogS.show();
                return true;

            //Usuario escoge el icon Agregar una nota nueva
            case R.id.btnAdd:
                addState();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Método auxiliar que se encarga de hacer ir al activity con los campos del
     * requeridos para poder agregar una nota
     */
    private void addState() {
        Intent intent = new Intent(this, CursoDetail.class);
        intent.putExtra("curso_Id", 0);
        startActivity(intent);
    }

    /**
     * Se encarga de llamar al método que abre la conexión con la base de datos y de traer los
     * datos de las notas
     *
     * @param usuario
     */
    public void SincronizarCursos(String usuario, Context context) {
        SincronizarCursos taskCursos;
        taskCursos = new SincronizarCursos(usuario, context);
        taskCursos.execute();
        while (taskCursos.sincronizacionNotas.equals("")) {
            //No haga nada
        }
        if (!taskCursos.sincronizacionNotas.equals("")) {
            int t = cursoList.size();
            int indice = t - 1;
            while (indice >= 0) {
                cursoList.remove(indice);
                --indice;
            }

            indice = 0;
            if (cursoList.size() == 0) {
                int k = taskCursos.cursoListSinc.size();
                HashMap<String, String> curso = new HashMap<String, String>();
                int p = k - 1;
                while (indice < k) {
                    curso.put("id", k + "");
                    curso.put("name", k + "");
                    ++indice;
                    cursoList.add(curso);
                }
                Collections.copy(cursoList, taskCursos.cursoListSinc);
                ListAdapter adapter = new SimpleAdapter(Cursos.this, cursoList, R.layout.view_curso_entry, new String[]{"id", "name"}, new int[]{R.id.curso_Id, R.id.curso_name});
                setListAdapter(adapter);
            }
        }
        returnHome();
    }

    /**
     * Método para revisar si hay conexión a internet
     *
     * @return boolean
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Metodo que permite volver a la página principal
     */
    public boolean returnHome() {
        Intent home_intent = new Intent(getApplicationContext(),
                MyMenu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
        return true;
    }

} //termina clase Cursos