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
import com.example.ark.ark.Sensors.Gps;

import java.util.Timer;
import java.util.TimerTask;


public class GpsFragment extends Fragment{
    private TextView x,y,z;
    private float[] gpsData;
    private Gps gps;
    private Timer timer;

    public GpsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gps, container, false);
        x=rootView.findViewById(R.id.gx);
        y=rootView.findViewById(R.id.gy);

        gps=new Gps(getActivity());
        gpsData=new float[2];
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(getActivity()==null)
                    return;
                gpsData=gps.getReading();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayData();;
                    }
                });
            }

        }, 0,500);

        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("GPS");

    }

    @SuppressLint("DefaultLocale")
    private void displayData(){
        x.setText(String.format("%.5f", gpsData[0]));
        y.setText(String.format("%.5f", gpsData[1]));
    }

}
