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
import com.example.ark.ark.Sensors.Magnetometer;
/**
 * Registered another listener in this fragment
 * such that data is displayed even when the service is switched off
 * instead of broadcasting the values from the service
 */
public class MagFragment extends Fragment implements SensorEventListener{
    private TextView x,y,z;
    private float[] magValues;
    private Sensor mag;
    private SensorManager magman;

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
        magman=(SensorManager)this.getActivity().getSystemService(Context.SENSOR_SERVICE);
        if(magman.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)!=null){
            magValues=new float[3];
            mag=magman.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            magman.registerListener(this,mag,SensorManager.SENSOR_DELAY_NORMAL);
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

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        for(int i=0;i<3;i++)
            magValues[i]=sensorEvent.values[i];
        displayData();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
