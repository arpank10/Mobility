package com.example.ark.ark.Services;


import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.ark.ark.Constants;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import static com.example.ark.ark.Constants.DIRECTORY;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.example.ark.ark.Constants;
import com.example.ark.ark.Services.DataRecording;
import com.example.ark.ark.Services.Internet_Broadcast;
import com.example.ark.ark.Services.uploading;
import com.example.ark.ark.app_url.app_config;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Comparator;

import javax.net.ssl.HttpsURLConnection;


import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import static com.example.ark.ark.Constants.DIRECTORY;

/**
 * Created by durgesh on 2/10/17.
 */

public class background_uploading extends IntentService {
    Context ctx;

    File sdDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + DIRECTORY);
    File accDirectory = new File(sdDirectory + Constants.DIRECTORY_ACC);
    File magDirectory = new File(sdDirectory + Constants.DIRECTORY_MAG);
    File gpsDirectory = new File(sdDirectory + Constants.DIRECTORY_GPS);
    File gyroDirectory = new File(sdDirectory + Constants.DIRECTORY_GYRO);
    File rotationDirectory = new File(sdDirectory + Constants.DIRECTORY_ROTATION);

    uploading u;

    public background_uploading() {
        super("bu");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        u = new uploading();
        Select_Dir();


    }

    public  boolean isNetworkAvailable() {
        boolean haveConnectedWifi = false;
        //boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
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



    public void Select_Dir(){
        File file;
        File[] files_acc  = accDirectory.listFiles();
        File[] files_mag  = magDirectory.listFiles();
        File[] files_gps  = gpsDirectory.listFiles();
        File[] files_rot  = rotationDirectory.listFiles();
        File[] files_gyro  = gyroDirectory.listFiles();
        if(files_acc!=null)
            Sort_files(files_acc);
        if(files_gps!=null)
            Sort_files(files_gps);
        if(files_mag!=null)
            Sort_files(files_mag);
        if(files_gyro!=null)
            Sort_files(files_gyro);
        if(files_rot!=null)
            Sort_files(files_rot);
        if(files_acc!=null) {
            while (files_acc.length > 1) {
                Log.d("durgesh", "continue");

                if (isNetworkAvailable()) {
                    file = files_acc[0];
                    Log.d("durgesh", file.getPath());
                    if (u.upload_file_to_server(file.getPath())) {
                        file.delete();
                        Log.d("delete", file.getPath());
                    }
                }
                files_acc = accDirectory.listFiles();
                if(files_acc!=null)
                    Sort_files(files_acc);
            }
        }
        if(files_mag!=null) {
            while (files_mag.length > 1) {
                if (isNetworkAvailable()) {
                    file = files_mag[0];
                    Log.d("durgesh", file.getPath());
                    if (u.upload_file_to_server(file.getPath())) {
                        file.delete();
                        Log.d("delete", file.getPath());
                    }
                }
                files_mag = magDirectory.listFiles();
                if(files_mag!=null)
                    Sort_files(files_mag);
            }
        }
        if(files_gyro!=null) {
            while (files_gyro.length > 1) {
                if (isNetworkAvailable()) {
                    file = files_gyro[0];
                    Log.d("durgesh", file.getPath());
                    if (u.upload_file_to_server(file.getPath())) {
                        file.delete();
                        Log.d("delete", file.getPath());
                    }
                }
                files_gyro = gyroDirectory.listFiles();
                if(files_gyro!=null)
                    Sort_files(files_gyro);
            }
        }
        if(files_gps!=null) {
            while (files_gps.length > 1) {
                if (isNetworkAvailable()) {
                    file = files_gps[0];
                    if (u.upload_file_to_server(file.getPath())) {
                        file.delete();
                        Log.d("delete ", file.getPath());
                    }
                    Log.d("durgesh", file.getPath());
                }
                files_gps = gpsDirectory.listFiles();
                if(files_gps!=null)
                    Sort_files(files_gps);
            }
        }
        if(files_rot!=null) {
            while (files_rot.length > 1) {
                if (isNetworkAvailable()) {
                    file = files_rot[0];
                    if (u.upload_file_to_server(file.getPath())) {
                        file.delete();
                        Log.d("delete ", file.getPath());
                    }
                    Log.d("durgesh", file.getPath());
                }
                files_rot = rotationDirectory.listFiles();
                if(files_rot!=null)
                    Sort_files(files_rot);
            }
        }
    }

    private void Sort_files(File[] files){
        Arrays.sort(files, new Comparator()
        {
            @Override
            public int compare(Object f1, Object f2) {
                return ((File) f1).getName().compareTo(((File) f2).getName());
            }
        });
    }

}



