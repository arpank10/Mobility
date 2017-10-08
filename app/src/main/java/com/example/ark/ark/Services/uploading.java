package com.example.ark.ark.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.ark.ark.Constants;
import com.example.ark.ark.R;
import com.example.ark.ark.activity.HomeActivity;
import com.example.ark.ark.activity.MainActivity;
import com.example.ark.ark.app_url.app_config;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Provider;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import static com.example.ark.ark.Constants.DIRECTORY;
import static com.google.android.gms.internal.zzagz.runOnUiThread;

/**
 * Created by durgesh on 2/10/17.
 */

public  class uploading   {

    //Declaring file variables
    String filename;
    HttpURLConnection conn = null;
    DataOutputStream dos = null;
    URL url = null;


    public boolean upload_file_to_server(String filename){


        Log.d("durgesh" , "NTKcommand");
        try {
            url = new URL(app_config.FILE_UPLOAD);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 100*1024*1024;


        try {
            Log.d("durgesh" , "entered");

            FileInputStream fileInputStream = new FileInputStream(filename);
            Log.d("durgesh" , "entered2");

            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("uploaded_file", filename);
            Log.d("durgesh" , "entered3");

            conn.connect();
            dos = new DataOutputStream(conn.getOutputStream());



            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""
                            + filename + "\";" + lineEnd);

            dos.writeBytes(lineEnd);
            Log.d("durgesh" , "entered4");

            // create a buffer of  maximum size
            bytesAvailable = fileInputStream.available();

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {

                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            }
            Log.d("durgesh" , "response");


            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            // Responses from the server (code and message)
            int serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            Log.i("uploadFile", "HTTP Response is : "
                    + serverResponseMessage + ": " + serverResponseCode);


            if(serverResponseCode == 200){

                conn.connect();
                Log.d("durgesh" , "res");

                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuffer response = new StringBuffer();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                Log.d("durgesh" ,response.toString());

                fileInputStream.close();
                reader.close();
                input.close();
                dos.flush();
                dos.close();


                 return  true;
            }
            else {
                fileInputStream.close();
                dos.flush();
                dos.close();
                return  false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return  false;
        }


    }

}
