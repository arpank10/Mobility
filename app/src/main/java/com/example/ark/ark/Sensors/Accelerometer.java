package com.example.ark.ark.Sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.ark.ark.fragments.AccFragment;

/**
 * Created by ark on 8/8/17.
 */

public class Accelerometer implements SensorEventListener {
    private Sensor acc;
    private SensorManager accman;
    private boolean accAvailable;
    private float[] accValue;

    public Accelerometer(Context context){
        accman=(SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        accAvailable=accman.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null;
        if(isAccAvailable()){
            accValue=new float[3];
            acc=accman.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            accman.registerListener(this,acc,SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        for(int i=0;i<3;i++)
            accValue[i]=event.values[i];
    }
    public float[] getLastReading(){
        return this.accValue;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    public boolean isAccAvailable(){
        return this.accAvailable;
    }
    public void unregister(){
        if(isAccAvailable())
            accman.unregisterListener(this,acc);
    }
}
