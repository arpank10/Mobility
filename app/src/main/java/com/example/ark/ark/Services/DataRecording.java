package com.example.ark.ark.Services;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.ark.ark.Constants;
import com.example.ark.ark.R;
import com.example.ark.ark.Sensors.Accelerometer;
import com.example.ark.ark.Sensors.Gps;
import com.example.ark.ark.Sensors.Gps_fusedlocationapi;
import com.example.ark.ark.Sensors.Gyroscope;
import com.example.ark.ark.Sensors.Magnetometer;
import com.example.ark.ark.Sensors.Rotation;
import com.example.ark.ark.activity.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static com.example.ark.ark.Constants.*;

/**
 * Created by ark on 10/10/17.
 */

@RequiresApi(api = Build.VERSION_CODES.N)
public class DataRecording extends Service {
    //Variables for classes
    private Magnetometer magnetometer;
    private Accelerometer accelerometer;
    private Gps gps;
    private Gyroscope gyro;
    private Rotation rot;
    //Timer variables
    private Timer myTimer, gpsTimer;
    private PowerManager.WakeLock wakelock; //wakelock
    //frequency variables
    private int acc_mag_freq = 100;
    private int gps_freq = 2000;

    //Time format
    SimpleDateFormat time = new SimpleDateFormat(TIMESTAMP_FORMAT);
    SimpleDateFormat time1 = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
    SimpleDateFormat time2 = new SimpleDateFormat("HH");

    //Declaring file variables
    private File sdDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + DIRECTORY);
    private File accDirectory = new File(sdDirectory + Constants.DIRECTORY_ACC);
    private File magDirectory = new File(sdDirectory + Constants.DIRECTORY_MAG);
    private File gpsDirectory = new File(sdDirectory + Constants.DIRECTORY_GPS);
    private File gyroDirectory = new File(sdDirectory + Constants.DIRECTORY_GYRO);
    private File rotationDirectory = new File(sdDirectory + Constants.DIRECTORY_ROTATION);
    private File datafile_Rot;
    private File dataFile_Acc;
    private File dataFile_Mag;
    private File dataFile_Gps;
    private File dataFile_Gyro;
    private FileOutputStream dataOutputStream_Acc, dataOutputStream_Mag, dataOutputStream_Gps, dataOutputStream_Rot,dataOutputStream_Gyro;
    //String builders for filename
    private StringBuilder data_Acc = new StringBuilder();
    private StringBuilder data_Mag = new StringBuilder();
    private StringBuilder data_Gps = new StringBuilder();
    private StringBuilder data_Rot = new StringBuilder();
    private StringBuilder data_Gyro = new StringBuilder();
    private String mode;
    private String acc_name, mag_name, rot_name, gps_name, gyro_name, user = "";
    private long count_acc = 0, count_gps = 0;
    private boolean should_record = true;

    @Override
    public void onCreate() {
        Intent notify = new Intent(this, MainActivity.class);
        PendingIntent pendingintent = PendingIntent.getActivity(this, 0, notify, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.icon_1_round)
                .setContentTitle("Mobility")
                .setContentText("Recording Data")
                .setContentIntent(pendingintent).build();

        startForeground(1337, notification);
        //Creating objects of the different sensor's class
        magnetometer = new Magnetometer(this);
        accelerometer = new Accelerometer(this);
        gps = new Gps(this);
        rot = new Rotation(this);
        gyro = new Gyroscope(this);

        data_Gps.append("\n");
        if(accelerometer.isAccAvailable())
        {
            data_Acc.append("\n");
        }
        if (magnetometer.isMagAvailable()) {
            data_Mag.append("\n");
        }
        if(rot.isRotAvailable())
        {
            data_Rot.append("\n");
        }
        if(gyro.isGyrAvailable()){
            data_Gyro.append("\n");
        }
        super.onCreate();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakelock= pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getCanonicalName());
        wakelock.acquire();
        if (intent != null && intent.getExtras() != null) {
            user = intent.getExtras().getString("username");
            String t = intent.getExtras().getString("acc_mag_freq");
            String r = intent.getExtras().getString("gps_freq");
            mode = intent.getExtras().getString("mode");
            acc_mag_freq = Integer.parseInt(t);
            gps_freq = Integer.parseInt(r);
        }

        //Creating variables for file names
        acc_name = user + "_" + time1.format(new Date()) + "_" + Constants.DATA_FILE_NAME_ACC;
        gps_name = user + "_" + time1.format(new Date()) + "_" + Constants.DATA_FILE_NAME_GPS;
        mag_name = user + "_" + time1.format(new Date()) + "_" + Constants.DATA_FILE_NAME_MAG;
        rot_name = user + "_" + time1.format(new Date()) + "_" + Constants.DATA_FILE_NAME_ROTATION;
        gyro_name = user + "_" + time1.format(new Date()) + "_" + Constants.DATA_FILE_NAME_GYRO;
        if(accelerometer.isAccAvailable())
        {
            dataFile_Acc = new File(accDirectory, acc_name);
        }
        if (magnetometer.isMagAvailable()) {
            dataFile_Mag = new File(magDirectory, mag_name);
        }
        if (gyro.isGyrAvailable()) {
            dataFile_Gyro = new File(gyroDirectory, gyro_name);
        }
        if(rot.isRotAvailable()){
            if (mode.equals("1"))
                datafile_Rot = new File(rotationDirectory, rot_name);
        }
        dataFile_Gps = new File(gpsDirectory, gps_name);

        try {
            if(accelerometer.isAccAvailable()){
                dataOutputStream_Acc = new FileOutputStream(dataFile_Acc, true);
            }
            if (magnetometer.isMagAvailable()) {
                dataOutputStream_Mag = new FileOutputStream(dataFile_Mag, true);
            }
            if (gyro.isGyrAvailable()) {
                dataOutputStream_Gyro = new FileOutputStream(dataFile_Gyro, true);
            }
            if(rot.isRotAvailable()){
                if (datafile_Rot != null)
                    dataOutputStream_Rot = new FileOutputStream(datafile_Rot, true);
            }
            dataOutputStream_Gps = new FileOutputStream(dataFile_Gps, true);

        } catch (IOException e) {
            e.printStackTrace();
        }

        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                int vt=Integer.parseInt(time2.format(new Date()));
                if(vt>=01&&vt<=05)
                    should_record=false;
                else
                    should_record=true;
                if(count_acc==Constants.TOTAL_RECORDINGS_ACC)
                {
                    acc_name=user+"_"+time1.format(new Date())+"_"+Constants.DATA_FILE_NAME_ACC;
                    mag_name=user+"_"+time1.format(new Date())+"_"+Constants.DATA_FILE_NAME_MAG;
                    gyro_name=user+"_"+time1.format(new Date())+"_"+Constants.DATA_FILE_NAME_GYRO;
                    rot_name=user+"_"+time1.format(new Date())+"_"+Constants.DATA_FILE_NAME_ROTATION;
                    if(accelerometer.isAccAvailable())
                    {
                        dataFile_Acc=new File(accDirectory,acc_name);
                    }
                    if(magnetometer.isMagAvailable())
                    {
                        dataFile_Mag=new File(magDirectory,mag_name);
                    }
                    if(gyro.isGyrAvailable())
                    {
                        dataFile_Gyro=new File(gyroDirectory,gyro_name);
                    }
                    if(rot.isRotAvailable())
                    {
                        if(mode.equals("1"))
                            datafile_Rot=new File(rotationDirectory,rot_name);
                    }
                    try {
                        if(accelerometer.isAccAvailable())
                        {
                            dataOutputStream_Acc = new FileOutputStream(dataFile_Acc, true);
                        }
                        if(magnetometer.isMagAvailable())
                        {
                            dataOutputStream_Mag=new FileOutputStream(dataFile_Mag,true);
                        }
                        if(gyro.isGyrAvailable())
                        {
                            dataOutputStream_Gyro=new FileOutputStream(dataFile_Gyro,true);
                        }
                        if(rot.isRotAvailable())
                        {
                            if(datafile_Rot!=null)
                                dataOutputStream_Rot=new FileOutputStream(datafile_Rot,true);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    count_acc=0;
                }
                Log.i("SensorDataRecord","Data recorded");
                getDataSensors();
            }

        }, 0, acc_mag_freq);
        gpsTimer = new Timer();
        gpsTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                if(count_gps==Constants.TOTAL_RECORDINGS_GPS)
                {
                    gps_name=user+"_"+time1.format(new Date())+"_"+Constants.DATA_FILE_NAME_GPS;
                    dataFile_Gps=new File(gpsDirectory,gps_name);
                    try {
                        dataOutputStream_Gps = new FileOutputStream(dataFile_Gps, true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    count_gps=0;
                }
                getDataGps();
                if(!isMyServiceRunning(ScreenOn.class)){
                    Intent intent1=new Intent(getApplicationContext(),ScreenOn.class);
                    startService(intent1);
                }
                if(isNetworkAvailable(getApplicationContext()))
                {
                    if(!isMyServiceRunning(background_uploading.class)){
                        Intent intent1=new Intent(getApplicationContext(),background_uploading.class);
                        startService(intent1);}
                }
            }
        }, 0, gps_freq);
        return START_STICKY;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void getDataGps() {
        data_Gps.append(time.format(new Date()));
        data_Gps.append(",");
        float[] gpsreading = gps.getReading();
        data_Gps.append(String.format("%.10f", gpsreading[0]));
        data_Gps.append(",");
        data_Gps.append(String.format("%.10f", gpsreading[1]));
        data_Gps.append("\n");
        try {
            if(should_record){
            count_gps++;
            dataOutputStream_Gps.write(data_Gps.toString().getBytes());
            }
            data_Gps.setLength(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getDataSensors() {
        if(accelerometer.isAccAvailable())
        {
            data_Acc.append(time.format(new Date()));
            data_Acc.append(",");
        }
        if (magnetometer.isMagAvailable()) {
            data_Mag.append(time.format(new Date()));
            data_Mag.append(",");
        }
        if (gyro.isGyrAvailable()) {
            data_Gyro.append(time.format(new Date()));
            data_Gyro.append(",");
        }
        if(rot.isRotAvailable())
        {
            if (mode.equals("1")) {
                data_Rot.append(time.format(new Date()));
                data_Rot.append(",");
            }
        }
        float[] acc = accelerometer.getLastReading();
        float[] mag = magnetometer.getLastReading();
        float[] gyr = gyro.getLastReading();
        float[] rotVal = rot.getLastReading();
        //accelerometer data_Acc
        if(accelerometer.isAccAvailable()) {
            data_Acc.append(String.format("%.3f", acc[0]));
            data_Acc.append(",");
            data_Acc.append(String.format("%.3f", acc[1]));
            data_Acc.append(",");
            data_Acc.append(String.format("%.3f", acc[2]));
            data_Acc.append("\n");
        }
            //data_Acc.append(",");
        if (magnetometer.isMagAvailable()) {
            //magnetometer data_Acc
            data_Mag.append(String.format("%.3f", mag[0]));
            data_Mag.append(",");
            data_Mag.append(String.format("%.3f", mag[1]));
            data_Mag.append(",");
            data_Mag.append(String.format("%.3f", mag[2]));
            //data_Acc.append(",");
            // write this data_Acc to file
            data_Mag.append("\n");
        }

        if(gyro.isGyrAvailable()) {
            data_Gyro.append(String.format("%.3f", gyr[0]));
            data_Gyro.append(",");
            data_Gyro.append(String.format("%.3f", gyr[1]));
            data_Gyro.append(",");
            data_Gyro.append(String.format("%.3f", gyr[2]));
            data_Gyro.append("\n");
        }

        if(rot.isRotAvailable())
        {
            if (mode.equals("1")) {
                data_Rot.append(String.format("%.3f", rotVal[0]));
                data_Rot.append(",");
                data_Rot.append(String.format("%.3f", rotVal[1]));
                data_Rot.append(",");
                data_Rot.append(String.format("%.3f", rotVal[2]));
                //data_Acc.append(",");
                // write this data_Acc to file
                data_Rot.append("\n");
            }
        }
        try {
            if(should_record==true){
            count_acc++;
            if(accelerometer.isAccAvailable())
            {
                dataOutputStream_Acc.write(data_Acc.toString().getBytes());
            }
            if (magnetometer.isMagAvailable()) {
                dataOutputStream_Mag.write(data_Mag.toString().getBytes());
            }
            if (gyro.isGyrAvailable()) {
                dataOutputStream_Gyro.write(data_Gyro.toString().getBytes());
            }
            if(rot.isRotAvailable())
            {
                if (mode.equals("1")) {
                    dataOutputStream_Rot.write(data_Rot.toString().getBytes());
                }
            }
            }
            data_Acc.setLength(0);
            data_Rot.setLength(0);
            data_Mag.setLength(0);
            data_Gyro.setLength(0);
            //Log.i("SensorData","RecordedInFile");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        accelerometer.unregister();
        magnetometer.unregister();
        rot.unregister();
        gyro.unregister();
        gps.unregister();
        if(gpsTimer!=null)
            gpsTimer.cancel();
        if(myTimer!=null)
            myTimer.cancel();
        wakelock.release();
        super.onDestroy();
    }


    public  boolean isNetworkAvailable(Context ctx) {
        boolean haveConnectedWifi = false;
        //boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnectedOrConnecting())
                    haveConnectedWifi = true;
            //if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
            //    if (ni.isConnectedOrConnecting())
            //        haveConnectedMobile = true;
        }
        return haveConnectedWifi;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
