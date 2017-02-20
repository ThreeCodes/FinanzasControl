package com.threecodes.finanzascontrol;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class GPS_Service extends Service implements LocationListener {

    private Context ctx;

    double latitud = 0, longitud = 0;
    Location location;
    boolean gpsA, netA;
    TextView texto;
    LocationManager locManager;


    public GPS_Service()
    {
        super();
        this.ctx = this.getApplicationContext();
    }

    public  GPS_Service(Context c)
    {
        super();
        this.ctx = c;
        getUb();
    }

    public void setView(View v) {
        texto = (TextView) v;
        texto.setText("Coordenadas: " + latitud + longitud);

    }

    public void getUb() {
        try {
            locManager = (LocationManager) this.ctx.getSystemService(LOCATION_SERVICE);
            gpsA = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            netA = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            Log.i("GPS_PROVIDER" + gpsA,"NETWORK_PROVIDER" + netA);

        } catch (Exception e) {e.printStackTrace();}


        try {
            if (gpsA) {
                locManager.requestLocationUpdates(locManager.GPS_PROVIDER, 20000, 10, this);

                location = locManager.getLastKnownLocation(locManager.GPS_PROVIDER);

                latitud = location.getLatitude();
                longitud = location.getLongitude();
            }
        } catch (Exception e) {e.printStackTrace();}


        Log.i("Latitud: " + latitud, "Longitud: " + longitud);


        try {
            if(netA && latitud == 0.0 && longitud == 0.0)
            {
                locManager.requestLocationUpdates(locManager.NETWORK_PROVIDER, 20000, 10, this);

                location = locManager.getLastKnownLocation(locManager.NETWORK_PROVIDER);

                latitud = location.getLatitude();
                longitud = location.getLongitude();
            }
        } catch (Exception e) {e.printStackTrace();}

    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
