package com.example.android.utime.app;

import android.app.ListActivity;
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


public class Notas extends ListActivity implements android.view.View.OnClickListener {

    Button btnAdd,btnGetAll;
    TextView nota_Id;

    /**
     *Evento al dar click en la vista
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view== findViewById(R.id.btnAdd)){

            Intent intent = new Intent(this,NotaDetail.class);
            intent.putExtra("nota_Id",0);
            startActivity(intent);
        }else {

            SQLControlador repo = new SQLControlador(this);

            ArrayList<HashMap<String, String>> notaList =  repo.getNotaList();
            if(notaList.size()!=0) {
                ListView lv = getListView();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                        nota_Id = (TextView) view.findViewById(R.id.nota_Id);
                        String notaId = nota_Id.getText().toString();
                        Intent objIndent = new Intent(getApplicationContext(),NotaDetail.class);
                        objIndent.putExtra("nota_Id", Integer.parseInt( notaId));
                        startActivity(objIndent);
                    }
                });
                ListAdapter adapter = new SimpleAdapter( Notas.this,notaList, R.layout.view_nota_entry, new String[] { "idNota","nameNota"}, new int[] {R.id.nota_Id, R.id.nota_name});
                setListAdapter(adapter);
            }else{
                Toast.makeText(this, "No existe la Nota!", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_notas);

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
        getMenuInflater().inflate(R.menu.notas, menu);
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
}