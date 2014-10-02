/*
 * Autores: Jennifer Ledezma
 *          Ana Laura Berdasco
 * Clase Cursos: Muestra los cursos guardados en la app por el usuario, y opción de interactuar
 *               de distintas formas con estos (crear un nuevo curso, editar, eliminar)
 */

package com.example.android.utime.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.ListActivity;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;


public class Cursos extends ListActivity implements android.view.View.OnClickListener {

    Button btnAdd,btnGetAll;
    TextView curso_Id;

    /**
     *Evento al dar click en la vista
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view== findViewById(R.id.btnAdd)){

            Intent intent = new Intent(this,CursoDetail.class);
            intent.putExtra("student_Id",0);
            startActivity(intent);

        }else {

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
                Toast.makeText(this, "No existe el Curso!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnGetAll = (Button) findViewById(R.id.btnGetAll);
        btnGetAll.setOnClickListener(this);

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
     * Seleccionar un item de la lista
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


} //termina clase Cursos