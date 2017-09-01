package com.example.ark.ark;

import android.hardware.SensorManager;


public final class Constants {
    //App related information
    public final static String APP_NAME = "Mobility";
    public final static String DIRECTORY = "/Mobility";
    public final static String DIRECTORY_ACC="/Accelerometer_Data";
    public final static String DIRECTORY_MAG="/Magnetometer_Data";
    public final static String DIRECTORY_GPS="/GPS_Data";
    public final static String DATA_FILE_NAME_ACC="accData.csv";
    public final static String DATA_FILE_NAME_MAG="magDATA.csv";
    public final static String DATA_FILE_NAME_GPS="gpsDATA.csv";
    public static String USER_NAME="";

    // Recording related info
    public final static String TIMESTAMP_FORMAT = "HH:mm:ss.SSS z";
    public final static int FAST_RECORDING_MODE = SensorManager.SENSOR_DELAY_FASTEST;
    public final static int MEDIUM_RECORDING_MODE = SensorManager.SENSOR_DELAY_GAME;
    public final static int NORMAL_RECORDING_MODE = SensorManager.SENSOR_DELAY_NORMAL;
    public final static int BATTERY_SAVER_RECORDING_MODE = SensorManager.SENSOR_DELAY_NORMAL;


}
