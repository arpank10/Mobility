package com.example.ark.ark.Services;


import android.app.NotificationManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
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

public class background_uploading extends AsyncTask<String ,Void , String> {
    // public static  String username;
    Context ctx;

    File sdDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + DIRECTORY);
    File accDirectory = new File(sdDirectory + Constants.DIRECTORY_ACC);
    File magDirectory = new File(sdDirectory + Constants.DIRECTORY_MAG);
    File gpsDirectory = new File(sdDirectory + Constants.DIRECTORY_GPS);
    File rotationDirectory = new File(sdDirectory + Constants.DIRECTORY_ROTATION);

    uploading u;

    NotificationManager mNotifyManager;
    NotificationCompat.Builder mBuilder;


    public background_uploading(Context context){

        ctx = context;
        mNotifyManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(ctx);
        mBuilder.setContentTitle("Uploading")
                .setContentText("uploading started");


    }

    @Override
    protected String doInBackground(String... strings) {
        //username = strings[0];
        u = new uploading();
        Select_Dir();


        return null;
    }



    public  boolean isNetworkAvailable() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnectedOrConnecting())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnectedOrConnecting())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }



    public void Select_Dir(){
        File file;
        File[] files_acc  = accDirectory.listFiles();
        File[] files_mag  = magDirectory.listFiles();
        File[] files_gps  = gpsDirectory.listFiles();
        File[] files_rot  = rotationDirectory.listFiles();
        Sort_files(files_acc);
        Sort_files(files_gps);
        Sort_files(files_mag);
        Sort_files(files_rot);
        while (files_acc.length > 1){
            Log.d("durgesh" , "continue");

            if(isNetworkAvailable()) {
                file = files_acc[0];
                Log.d("durgesh" , file.getPath());
                if (u.upload_file_to_server(file.getPath())){
                    file.delete();
                    Log.d("delete" , file.getPath());
                }
            }
            files_acc = accDirectory.listFiles();
            Sort_files(files_acc);
        }
        while (files_mag.length > 1){
            if(isNetworkAvailable()) {
                file = files_mag[0];
                Log.d("durgesh" , file.getPath());
                if (u.upload_file_to_server(file.getPath())){
                    file.delete();
                    Log.d("delete" , file.getPath());
                }
            }
            files_mag = magDirectory.listFiles();
        }
        while (files_gps.length > 1){
            if(isNetworkAvailable()) {
                file = files_gps[0];
                if (u.upload_file_to_server(file.getPath())){
                    file.delete();
                    Log.d("delete " , file.getPath());
                }
                Log.d("durgesh" , file.getPath());
            }
            files_gps = gpsDirectory.listFiles();
        }
        while (files_rot.length > 1){
            if(isNetworkAvailable()) {
                file = files_rot[0];
                if (u.upload_file_to_server(file.getPath())){
                    file.delete();
                    Log.d("delete " , file.getPath());
                }
                Log.d("durgesh" , file.getPath());
            }
            files_rot = rotationDirectory.listFiles();
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
/*
    @Override
    protected void onProgressUpdate(Void... values) {
        mBuilder.setContentTitle("Uploading")
                .setContentText("uploading in progress");
    }
    @Override
    protected void onPostExecute(String result) {
        mBuilder.setContentTitle("Uploading")
                .setContentText("uploading finished");
    }
*/



/*

    Context  context;
    AlertDialog alertdialog ;
    String result;

     background_register(Context ctx){
         context = ctx;

     }
    URL url ;
    HttpURLConnection urlConnection = null;


    @Override
    protected String doInBackground(String... strings) {


         username = strings[0];
        String password = strings[1];
        String email = strings[2];

        try {
            url = new URL(app_config.URL_REGISTER);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return new String("Exception: " + e.getMessage());
        }

        try {


            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
           urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.connect();


            OutputStream outputStream = urlConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                    URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&" +
                    URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();


            int responseCode= urlConnection.getResponseCode();
            String res = String.valueOf(responseCode);



            if (responseCode == 200) {
                urlConnection.connect();

                InputStream input = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuffer response = new StringBuffer();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                input.close();
                return response.toString();
            }
            else {
                return new String("false : "+responseCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return new String("Exception: " + e.getMessage());
        }


    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }


    @Override
    protected void onPostExecute(String result) {
        super.onPreExecute();
        Toast.makeText(context, result,
                Toast.LENGTH_LONG).show();

        if(result == "success"){
            app_config.logging = 1;

            Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(context, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
        }

    }

*/

}



