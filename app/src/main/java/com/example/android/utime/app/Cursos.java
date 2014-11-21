/*
 * Autores: Jennifer Ledezma
 *          Ana Laura Berdasco
 * Clase Cursos: Muestra los cursos guardados en la app por el usuario, y opción de interactuar
 *               de distintas formas con estos (crear un nuevo curso, editar, eliminar)
 */

package com.example.android.utime.app;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import java.util.HashMap;

public class Cursos extends ListActivity {

    TextView curso_Id;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);

        SQLControlador repo = new SQLControlador(this);

        ArrayList<HashMap<String, String>> cursoList =  repo.getCursoList();
        if(cursoList.size()!=0) {
            ListView lv = getListView();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                    curso_Id = (TextView) view.findViewById(R.id.curso_Id);
                    String cursoId = curso_Id.getText().toString();
                    Intent objIndent = new Intent(getApplicationContext(),CursoDetail.class);
                    objIndent.putExtra("curso_Id", Integer.parseInt( cursoId));
                    startActivity(objIndent);
                }
            });
            ListAdapter adapter = new SimpleAdapter( Cursos.this,cursoList, R.layout.view_curso_entry, new String[] { "id","name"}, new int[] {R.id.curso_Id, R.id.curso_name});
            setListAdapter(adapter);
        }else{
            Toast.makeText(this, "No has ingresado cursos!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *Evento para mostrar una actiividad
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
                                "Students: \n"+
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

} //termina clase Cursos