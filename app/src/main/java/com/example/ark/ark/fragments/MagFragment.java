package com.example.ark.ark.fragments;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ark.ark.R;
import com.example.ark.ark.Sensors.Accelerometer;
import com.example.ark.ark.Sensors.Magnetometer;
import com.google.android.gms.internal.acc;

import java.util.Timer;
import java.util.TimerTask;


public class MagFragment extends Fragment{
    private TextView x,y,z;
    private float[] magValues;
    private Magnetometer mag;
    private Timer timer;

    public MagFragment() {
        // Required empty public constructor
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View rootView = inflater.inflate(R.layout.fragment_mag, container, false);
        x=rootView.findViewById(R.id.mx);
        y=rootView.findViewById(R.id.my);
        z=rootView.findViewById(R.id.mz);
        mag=new Magnetometer(getActivity());
        if(mag.isMagAvailable()) {
            magValues = new float[3];
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (getActivity() == null)
                        return;
                    magValues = mag.getLastReading();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayData();
                            ;
                        }
                    });
                }

            }, 0, 100);
        }
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Magnetometer");
    }

    private void displayData()
    {
        x.setText(String.format("%.5f", magValues[0]));
        y.setText(String.format("%.5f", magValues[1]));
        z.setText(String.format("%.5f", magValues[2]));

    }

}
