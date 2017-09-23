package com.example.ark.ark.Sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by ark on 5/9/17.
 */

public class Rotation implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor acc,mag;
    // Gravity rotational data
    private float gravity[];
    // Magnetic rotational data
    private float magnetic[]; //for magnetic rotational data
    private float accels[] = new float[3];
    private float mags[] = new float[3];
    private float[] values = new float[3];


    // azimuth, pitch and roll
    private float azimuth;
    private float pitch;
    private float roll;

    public Rotation(Context con){
        sensorManager=(SensorManager)con.getSystemService(Context.SENSOR_SERVICE);
        acc=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mag=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this,acc,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,mag,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_MAGNETIC_FIELD:
                mags = sensorEvent.values.clone();
                break;
            case Sensor.TYPE_ACCELEROMETER:
                accels = sensorEvent.values.clone();
                break;
        }

        if (mags != null && accels != null) {
            gravity = new float[9];
            magnetic = new float[9];
            SensorManager.getRotationMatrix(gravity, magnetic, accels, mags);
            float[] outGravity = new float[9];
            SensorManager.remapCoordinateSystem(gravity, SensorManager.AXIS_X,SensorManager.AXIS_Z, outGravity);
            SensorManager.getOrientation(outGravity, values);

            values[0]= values[0] * 57.2957795f;
            values[1]=values[1] * 57.2957795f;
            values[2]= values[2] * 57.2957795f;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    public float[] getLastReading(){
      return this.values;
    }
}
