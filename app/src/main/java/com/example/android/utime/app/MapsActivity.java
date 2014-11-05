/*
 * Autores: Jennifer Ledezma
 *
 * Clase MapsActivity: Se encarga de las funcionalidades referentes a las ubicaciones más importantes
 *                     de la Universidad de Costa Rica
 */

package com.example.android.utime.app;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;

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

public class MapsActivity extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks,
                                                              com.google.android.gms.location.LocationListener,
                                                              GooglePlayServicesClient.OnConnectionFailedListener{

    private GoogleMap mMap;                 // Referencia al mapa
    private CameraUpdate miposicion;
    private LocationClient miLocalizacion;
    private Button buscar;

    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)              // 5 segundos
            .setFastestInterval(16)         // Conversion 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        levantarLocalizacion();
        miLocalizacion.connect();
    }

    @Override
    public void onPause(){
        super.onPause();
        if(miLocalizacion != null){
            miLocalizacion.disconnect();
        }
    }

    /**
     * Medodo que establece mi localización como conectada
     * @param bundle
     */
    @Override
    public void onConnected(Bundle bundle) {
        // This= Localizacion Listener
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
    @Override
    public void onLocationChanged(Location location) {
        verMiUbicacion(location.getLatitude(), location.getLongitude());
    }

    /**
     * Metodo que debe se debe sobreescribir pero no es usado
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */

    /**
     *  Obtiene una referencia  del Mapa de objeto si no existe ninguno y lo pone el color icono de
     *  la flecha en el mapa
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {

                mMap.setMyLocationEnabled(true);
                mMap.setMyLocationEnabled(true); //Es para activar mi poscion
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        mMap.addMarker(new MarkerOptions().position(new LatLng(9.939667, -84.047341)).title("Marker")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .snippet("Universidad de Costa Rica"));
        miposicion = CameraUpdateFactory.newLatLngZoom(new LatLng(9.939667, -84.047341), 16);
        mMap.animateCamera(miposicion);
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
        mMap.moveCamera(update);
    }

}
