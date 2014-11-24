/*
 * Autores: Jennifer Ledezma
 *          Ana Laura Berdasco
 *
 * Clase MapsActivity: Se encarga de las funcionalidades referentes a las ubicaciones más importantes
 *                     de la Universidad de Costa Rica, así como el Buscar una localidad
 */

package com.example.android.utime.app;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity {

    GoogleMap googleMap;
    MarkerOptions markerOptions;
    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);

        // Obtiene  una referencia al mapa
        googleMap = supportMapFragment.getMap();

        // Obtiene la referencia del boton de buscar
        Button btn_find = (Button) findViewById(R.id.btn_find);

        // Define el evento que se debe mostrar al dar click a buscar
        OnClickListener findClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtiene referencia del edit text para escribir la ubicacion a buscar
                EditText etLocation = (EditText) findViewById(R.id.et_location);

                // Obtiene el string a buscar ingresado por el usuario
                String location = etLocation.getText().toString();

                if(location!=null && !location.equals("")){
                    new GeocoderTask().execute(location);
                }
            }
        };

        // establece el onclick
        btn_find.setOnClickListener(findClickListener);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_maps, menu);
        return true;
    }

    /**
     * Clase privada que permite acceder al Servicio Web GeoCoding mediante AsyncTask
     *
     * Autores: Ana Laura Berdasco
     *          Jennifer Ledezma
     */
    private class GeocoderTask extends AsyncTask<String, Void, List<Address>>{

        @Override
        protected List<Address> doInBackground(String... locationName) {
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;

            try {
                //Obtiene un maximo de 3 direcciones segun el texto insertado por el usuario
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        /**
         * Metodo que se encarga de limpiar y actualizar el marker en la ubicacion encontrada
         * @param addresses
         */
        @Override
        protected void onPostExecute(List<Address> addresses) {

            if(addresses==null || addresses.size()==0){
                Toast.makeText(getBaseContext(), "No se encuentra la Ubicación", Toast.LENGTH_SHORT).show();
            }

            // Limpia todos los markers existentes en el mapa
            googleMap.clear();

            // Posiciona un marker por la dir buscada
            for(int i=0;i<addresses.size();i++){

                Address address = (Address) addresses.get(i);

                latLng = new LatLng(address.getLatitude(), address.getLongitude());

                String addressText = String.format("%s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getCountryName());

                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(addressText);

                googleMap.addMarker(markerOptions);

                // Localiza la primer ubicacion
                if(i==0)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    }
}

