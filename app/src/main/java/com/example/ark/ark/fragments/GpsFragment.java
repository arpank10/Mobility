package com.example.ark.ark.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ark.ark.R;
/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("FieldCanBeLocal")
public class GpsFragment extends Fragment implements LocationListener{
    private TextView x,y,z;
    private Location loc;
    private LocationManager locman;
    private boolean isgps;
    private boolean isnetwork;
    private float[] gpsdata;

    private final String gps_provider = LocationManager.GPS_PROVIDER;
    private final String network_provider = LocationManager.NETWORK_PROVIDER;


    public GpsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gps, container, false);
        x=rootView.findViewById(R.id.gx);
        y=rootView.findViewById(R.id.gy);
        z=rootView.findViewById(R.id.gz);
        gpsdata=new float[3];
        locman = (LocationManager)this.getActivity().getSystemService(Context.LOCATION_SERVICE);

        isgps = locman.isProviderEnabled(gps_provider);
        isnetwork = locman.isProviderEnabled(network_provider);


        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        locman.requestLocationUpdates(gps_provider, 0, 0, this);
        loc=locman.getLastKnownLocation(gps_provider);

        if(loc== null)
            if (isnetwork)
                loc = locman.getLastKnownLocation(network_provider);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("GPS");
    }

    @Override
    public void onLocationChanged(Location location) {
        loc=location;
        displayData();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @SuppressLint("DefaultLocale")
    private void displayData(){
        if(loc!=null)
        {
            gpsdata[0]= (float) loc.getLatitude();
            gpsdata[1]=(float) loc.getLongitude();
            gpsdata[2]=(float) loc.getAltitude();
        }
        x.setText(String.format("%.5f", gpsdata[0]));
        y.setText(String.format("%.5f", gpsdata[1]));
        z.setText(String.format("%.5f", gpsdata[2]));
    }
    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
