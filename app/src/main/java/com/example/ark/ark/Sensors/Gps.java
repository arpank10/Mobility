package com.example.ark.ark.Sensors;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Created by ark on 20/8/17.
 */

public class Gps implements LocationListener {
    private Location loc;
    private Context context;
    private final LocationManager locman;
    private final boolean isgps;
    private final boolean isnetwork;

    private final String gps_provider = LocationManager.GPS_PROVIDER;
    private final String network_provider = LocationManager.NETWORK_PROVIDER;

    public Gps(Context con) {
        context=con;
        locman = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        isgps = locman.isProviderEnabled(gps_provider);
        isnetwork = locman.isProviderEnabled(network_provider);


        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }
        locman.requestLocationUpdates(gps_provider, 0, 0, this);
            loc=locman.getLastKnownLocation(gps_provider);

        if(loc== null)
            if (isnetwork) {
                locman.requestLocationUpdates(network_provider,0,0,this);
                loc = locman.getLastKnownLocation(network_provider);
            }
    }

    @Override
    public void onLocationChanged(Location location) {
        loc=location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public float[] getReading(){
        float[] gpsdata=new float[2];
        gpsdata[0]=0;gpsdata[1]=0;
        if(loc!=null)
        {
        gpsdata[0]= (float) loc.getLatitude();
        gpsdata[1]=(float) loc.getLongitude();
        }
        return gpsdata;

    }
}
