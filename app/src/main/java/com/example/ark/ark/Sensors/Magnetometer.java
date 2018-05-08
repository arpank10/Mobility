package com.example.ark.ark.Sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.ark.ark.Services.DataRecording;
import com.example.ark.ark.fragments.MagFragment;

/**
 * Created by ark on 8/8/17.
 */

public class Magnetometer implements SensorEventListener {
    private Sensor mag;
    private SensorManager magman;
    private boolean magAvailable;
    private float[] magValue;

    public  Magnetometer(Context context){
        magman=(SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        magAvailable=magman.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)!=null;
        if(isMagAvailable()){
            magValue=new float[3];
            mag=magman.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            magman.registerListener(this,mag,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        for(int i=0;i<3;i++)
            magValue[i]=event.values[i];
    }
    public float[] getLastReading(){
        return this.magValue;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    public boolean isMagAvailable(){
        return this.magAvailable;
    }
    public void unregister(){
        if(isMagAvailable())
            magman.unregisterListener(this,mag);
    }
}
