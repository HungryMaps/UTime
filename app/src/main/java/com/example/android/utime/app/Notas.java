/*
 * Autores: Jennifer Ledezma
 *          Ana Laura Berdasco
 * Clase Cursos: Muestra las notas en la app por el usuario, y opción de interactuar
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
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class Notas extends ListActivity {
    private static final int DELETE_ID = Menu.FIRST;
    private int mNoteNumber = 1;


    Button btnAdd;
    TextView nota_Id;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);

        SQLControlador repo = new SQLControlador(this);

        ArrayList<HashMap<String, String>> notaList = repo.getNotaList();
        if (notaList.size() != 0) {
            ListView lv = getListView();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    nota_Id = (TextView) view.findViewById(R.id.nota_Id);
                    String notaId = nota_Id.getText().toString();
                    Intent objIndent = new Intent(getApplicationContext(), NotaDetail.class);
                    objIndent.putExtra("nota_Id", Integer.parseInt(notaId));
                    startActivity(objIndent);
                }
            });
            ListAdapter adapter = new SimpleAdapter(Notas.this, notaList, R.layout.view_nota_entry, new String[]{"idNota", "nameNota"}, new int[]{R.id.nota_Id, R.id.nota_name});
            setListAdapter(adapter);
        } else {
            Toast.makeText(this, "No existe la Nota!", Toast.LENGTH_SHORT).show();
        }
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
        getMenuInflater().inflate(R.menu.notas, menu);
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

                AlertDialog.Builder dialog = new AlertDialog.Builder(Notas.this);
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

            //Usuario escoge el icon de: Papelera de Reciclaje en el MenuBar
            case R.id.btnAdd:
                addState();
                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addState() {
        Intent intent = new Intent(this, NotaDetail.class);
        intent.putExtra("nota_Id", 0);
        startActivity(intent);
    }

}