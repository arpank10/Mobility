package com.example.ark.ark.Sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by ark on 8/5/18.
 */

public class Gyroscope implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sens;
    private float[] values1 = new float[3];
    private boolean available;

    public Gyroscope(Context con){
        sensorManager=(SensorManager)con.getSystemService(Context.SENSOR_SERVICE);
        available=(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)!=null);
        if(available)
            sens=sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(this,sens,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        for(int i=0;i<3;i++)
            values1[i]=sensorEvent.values[i];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    public boolean isGyrAvailable(){
        return this.available;
    }
    public float[] getLastReading(){
        return this.values1;
    }

    public void unregister(){
        sensorManager.unregisterListener(this,sens);
    }
}
