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

/**
 * Registered another sensoreventlistener in this fragment
 * such that data is displayed even when the service is switched off
 */
public class AccFragment extends Fragment implements SensorEventListener{
    private TextView x,y,z;
    private float[] accValues;
    private Sensor acc;
    private SensorManager accman;
    public AccFragment() {
        // Required empty public constructor
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View rootView = inflater.inflate(R.layout.fragment_accfragment, container, false);
        x=rootView.findViewById(R.id.ax);
        y=rootView.findViewById(R.id.ay);
        z=rootView.findViewById(R.id.az);

        accman=(SensorManager)this.getActivity().getSystemService(Context.SENSOR_SERVICE);
        if(accman.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null){
            accValues=new float[3];
            acc=accman.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            accman.registerListener(this,acc,SensorManager.SENSOR_DELAY_NORMAL);
        }

        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Accelerometer");
    }
    public void displayData()
    {
        x.setText(String.format("%.5f", accValues[0]));
        y.setText(String.format("%.5f", accValues[1]));
        z.setText(String.format("%.5f", accValues[2]));
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        for(int i=0;i<3;i++)
            accValues[i]=sensorEvent.values[i];
        displayData();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
