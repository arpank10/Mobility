package com.example.ark.ark.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ark.ark.R;
import com.example.ark.ark.Sensors.Gps_fusedlocationapi;

import java.util.Timer;
import java.util.TimerTask;


public class GpsFragment extends Fragment{
    private TextView x,y;
    private float[] gpsData;
    private Gps_fusedlocationapi gps;
    private Timer timer;
    View rootView;
    public GpsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_gps, container, false);
        x=rootView.findViewById(R.id.gx);
        y=rootView.findViewById(R.id.gy);

        gps=new Gps_fusedlocationapi(getActivity().getApplicationContext());
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
    @Override
    public void onDestroyView() {
        rootView=null;
        if(timer!=null)
            timer.cancel();
        super.onDestroyView();
    }

}
