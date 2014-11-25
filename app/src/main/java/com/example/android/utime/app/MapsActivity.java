/*
 * Autores: Jennifer Ledezma
 *          Ana Laura Berdasco
 *
 * Clase MapsActivity: Se encarga de las funcionalidades referentes a las ubicaciones más importantes
 *                     de la Universidad de Costa Rica
 */

package com.example.android.utime.app;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks,
                                                              GooglePlayServicesClient.OnConnectionFailedListener,
                                                              com.google.android.gms.location.LocationListener{

    GoogleMap googleMap;
    MarkerOptions markerOptions;
    LatLng latLng;
    private CameraUpdate miposicion;
    private LocationClient miLocalizacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        //Obtiene referencia del mapa
        googleMap = supportMapFragment.getMap();

        Button btn_find = (Button) findViewById(R.id.btn_find);

        /**
         * Hace la funcionalidad del buscar al dar el click
         */
        OnClickListener findClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText etLocation = (EditText) findViewById(R.id.et_location);
                //Obtiene el string de la localidad que desea buscar el usuario
                String location = etLocation.getText().toString();

                if(location!=null && !location.equals("")){
                    new GeocoderTask().execute(location);
                }
            }
        };

        //Establece el evento al que debe redirigir el dar click
        btn_find.setOnClickListener(findClickListener);
    }

    /**
     * Mantiene las funcionalidades del MenuBar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return true;
    }

    /**
     * Para pedir la localizacion
     */
    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)              // 5 segundos
            .setFastestInterval(16)         // Conversion 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    /**
     *  Se llama cuando la actividad va a comenzar a interactuar con el usuario.
     */
    @Override
    protected void onResume() {
        super.onResume();
        levantarLocalizacion();
        miLocalizacion.connect();
    }

    /**
     * Indica que la actividad está a punto de ser lanzada a segundo plano
     */
    @Override
    public void onPause(){
        super.onPause();
        if(miLocalizacion != null){
            miLocalizacion.disconnect();
        }
    }

    /**
     * Metodo que debe se debe sobreescribir pero no es usado
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /**
     * Medodo que establece mi localización como conectada
     * @param bundle
     */
    @Override
    public void onConnected(Bundle bundle) {
        miLocalizacion.requestLocationUpdates( REQUEST, this);
    }

    /**
     * Metodo que debe se debe sobreescribir pero no es usado
     */
    @Override
    public void onDisconnected() {

    }

    /**
     * Se encarga la llamar a ver mi ubicación esto para cada vez que se cambia nuestra ubicación
     * @param location Localización del objeto con toda la información acerca de la ubicación
     */
     public void onLocationChanged(Location location) {
        verMiUbicacion(location.getLatitude(), location.getLongitude());
    }

    /**
     * Cuando hay un dispotivo en la app crea un objeto de localizacion
     * del cliente si no hay ninguno
     */
    private void levantarLocalizacion() {
        if(miLocalizacion == null){
            //El primer this indica llamado a la conexión
            //Segundo this indica que el llamado a la conexión falló
            miLocalizacion = new LocationClient(getApplicationContext(), this, this);
        }
    }

    /**
     * Metodo que permite poner mi ubicacion en el mapa
     * @param lat Referente a la latitud
     * @param lng Referente a la longitus
     */
    private void verMiUbicacion(double lat, double lng) {
        cambiarCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(lat, lng))
                        .zoom(16)
                        .bearing(0)              //Establece la orientacion
                        .tilt(25)                // Baja el punto de vista de la camara 25 grados
                        .build()
        ), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                // Your code here to do something after the Map is rendered
            }

            @Override
            public void onCancel() {
                // Your code here to do something after the Map rendering is cancelled
            }
        });
    }

    private void cambiarCamera(CameraUpdate update, GoogleMap.CancelableCallback callback) {
        googleMap.moveCamera(update);
    }


      /************************************************************************
     * Clase privada que por medio de AsyncTask accesa el Geocoding Web Service
     * para las funcionalidades de Buscar, tranforma el string en coordenadas
     * para ubicar el marker en el mapa
     ****************************************************************************/
    private class GeocoderTask extends AsyncTask<String, Void, List<Address>>{

        @Override
        protected List<Address> doInBackground(String... locationName) {

            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;

            try {
                //Obtiene un maximo de 3 direcciones que coinciden con el texto ingresado
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        /**
         * Ubica un Marker en la Ubicacion encontrada, muestra al menos 3 coincidencias
         * al usuario, de así encontrar más de una coincidencia
         * @param addresses
         */

        @Override
        protected void onPostExecute(List<Address> addresses) {

            if(addresses==null || addresses.size()==0){
                Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            }
            //Limpia markers existentes en el mapa
            googleMap.clear();

            //Agrega un marker en el mapa de la localidad encontrada
            for(int i=0;i<addresses.size();i++){

                Address address = (Address) addresses.get(i);

                //Crea una instancia Geopoint para mostrar el mapa
                latLng = new LatLng(address.getLatitude(), address.getLongitude());

                String addressText = String.format("%s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getCountryName());

                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(addressText);

                googleMap.addMarker(markerOptions);

                //Muestra la primer ubicacion encontrada
                if(i==0)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                if(i!=0){
                    miposicion = CameraUpdateFactory.newLatLngZoom(new LatLng(9.939667, -84.047341), 16);
                    googleMap.animateCamera(miposicion);
                }
            }
        }
    }
}