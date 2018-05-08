package com.example.ark.ark.Sensors;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
/**
 * Created by ark on 12/10/17.
 */

public class Gps_fusedlocationapi implements
        GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks, com.google.android.gms.location.LocationListener {
    private GoogleApiClient mGoogleApiClient;
    private Context context;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    boolean connected=false;

    public Gps_fusedlocationapi(Context con){
        context=con;
        buildapiclient();
    }
    
    protected synchronized void buildapiclient(){
        mGoogleApiClient = new GoogleApiClient.Builder(context.getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
    connected=true;
    mLocationRequest=new LocationRequest().setInterval(1000).setFastestInterval(1000)
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
            Log.d("Fused","Connection Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Fused","Connection Failed");
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

    }

    public float[] getReading(){
        float[] gpsdata=new float[2];
        gpsdata[0]=0;gpsdata[1]=0;
        if(mLastLocation!=null)
        {
            gpsdata[0]= (float) mLastLocation.getLatitude();
            gpsdata[1]=(float) mLastLocation.getLongitude();
        }
        return gpsdata;
    }

    public boolean isConnected(){
        return this.connected;
    }

    public void unregister()
    {
        context=null;
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
        mGoogleApiClient.disconnect();
        Log.d("Fused","Disconnected");
    }

}
