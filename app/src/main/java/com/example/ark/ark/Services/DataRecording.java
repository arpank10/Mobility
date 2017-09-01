package com.example.ark.ark.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.NotificationCompat;

import com.example.ark.ark.Constants;
import com.example.ark.ark.R;
import com.example.ark.ark.Sensors.Accelerometer;
import com.example.ark.ark.Sensors.Gps;
import com.example.ark.ark.Sensors.Magnetometer;
import com.example.ark.ark.activity.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static com.example.ark.ark.Constants.*;

/**
 * Created by ark on 12/8/17.
 */

@RequiresApi(api = Build.VERSION_CODES.N)
public class DataRecording extends Service {
    private Magnetometer magnetometer;
    private Accelerometer accelerometer;
    private Gps gps;
    private Timer myTimer,gpsTimer;
    //frequency variables
    private int acc_mag_freq=100;
    private int gps_freq=2000;

    SimpleDateFormat time=new SimpleDateFormat(TIMESTAMP_FORMAT);
    SimpleDateFormat time1=new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");

    //Declaring file variables
    private File sdDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + DIRECTORY);
    File accDirectory=new File(sdDirectory+ Constants.DIRECTORY_ACC);
    File magDirectory=new File(sdDirectory+Constants.DIRECTORY_MAG);
    File gpsDirectory=new File(sdDirectory+Constants.DIRECTORY_GPS);
    private File dataFile_Acc;
    private File dataFile_Mag;
    private File dataFile_Gps;
    private FileOutputStream dataOutputStream_Acc,dataOutputStream_Mag,dataOutputStream_Gps;

    private StringBuilder data_Acc =new StringBuilder();
    private StringBuilder data_Mag =new StringBuilder();
    private StringBuilder data_Gps =new StringBuilder();
    @Override
    public void onCreate() {
        Intent notify= new Intent(this, MainActivity.class);
        PendingIntent pendingintent=PendingIntent.getActivity(this,0,notify,0);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.icon_1_round)
                .setContentTitle("Mobility")
                .setContentText("Recording Data")
                .setContentIntent(pendingintent).build();

        startForeground(1337, notification);
        //Creating objects of the different sensor's class
        magnetometer=new Magnetometer(this);
        accelerometer=new Accelerometer(this);
        gps=new Gps(this);


        data_Gps.append("\n");
        data_Acc.append("\n");
        if(magnetometer.isMagAvailable())
            data_Mag.append("\n");

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        String user="";
        if (intent !=null && intent.getExtras()!=null){
            user = intent.getExtras().getString("username");
            String t=intent.getExtras().getString("acc_mag_freq");
            String r=intent.getExtras().getString("gps_freq");
            acc_mag_freq=Integer.parseInt(t);
            gps_freq=Integer.parseInt(r);
        }

        //Creating variables for file names
        String acc_name=user+"_"+time1.format(new Date())+"_"+Constants.DATA_FILE_NAME_ACC;
        String gps_name=user+"_"+time1.format(new Date())+"_"+Constants.DATA_FILE_NAME_GPS;
        String mag_name=user+"_"+time1.format(new Date())+"_"+Constants.DATA_FILE_NAME_MAG;

        dataFile_Acc=new File(accDirectory,acc_name);
        if(magnetometer.isMagAvailable())
            dataFile_Mag=new File(magDirectory,mag_name);
        dataFile_Gps=new File(gpsDirectory,gps_name);

        try {
            dataOutputStream_Acc = new FileOutputStream(dataFile_Acc, true);
            if(magnetometer.isMagAvailable())
                dataOutputStream_Mag=new FileOutputStream(dataFile_Mag,true);
            dataOutputStream_Gps=new FileOutputStream(dataFile_Gps,true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0,acc_mag_freq);
        gpsTimer=new Timer();
        gpsTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                getDataGps();
            }
        },0,gps_freq);
        return START_STICKY;
    }
    public void TimerMethod(){
        getDataSensors();
    }
    public void getDataGps(){
        data_Gps.append(time.format(new Date()));
        data_Gps.append(",");
        float[] gpsreading=gps.getReading();
        data_Gps.append(String.format("%.10f",gpsreading[0]));
        data_Gps.append(",");
        data_Gps.append(String.format("%.10f",gpsreading[1]));
        data_Gps.append("\n");
        try {
            dataOutputStream_Gps.write(data_Gps.toString().getBytes());
            data_Gps.setLength(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getDataSensors(){
        data_Acc.append(time.format(new Date()));
        data_Acc.append(",");
        if(magnetometer.isMagAvailable()){
            data_Mag.append(time.format(new Date()));
            data_Mag.append(",");}
        float[] acc= accelerometer.getLastReading();
        float[] mag = magnetometer.getLastReading();
        //accelerometer data_Acc
        data_Acc.append(String.format("%.3f", acc[0]));
        data_Acc.append(",");
        data_Acc.append(String.format("%.3f", acc[1]));
        data_Acc.append(",");
        data_Acc.append(String.format("%.3f", acc[2]));
        //data_Acc.append(",");
        if(magnetometer.isMagAvailable()) {
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
        data_Acc.append("\n");
        try {
            dataOutputStream_Acc.write(data_Acc.toString().getBytes());
            if(magnetometer.isMagAvailable())
                dataOutputStream_Mag.write(data_Mag.toString().getBytes());
            data_Acc.setLength(0);
            if(magnetometer.isMagAvailable())
                data_Mag.setLength(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
