package com.example.ark.ark.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ark.ark.activity.MainActivity;

/**
 * Created by durgesh on 5/10/17.
 */

public class Internet_Broadcast extends BroadcastReceiver {
    Context ctx;

    @Override
    public void onReceive(Context context, Intent intent) {
        ctx = context;
        Log.d("Network Available ", "Flag No 1 entered");

        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        SharedPreferences pref = context.getSharedPreferences("ActivityPREF" , 0);
        String username = pref.getString("username" , "user");

        if (isNetworkAvailable()) {
            Log.d("Network Available ", "Flag No 1 entered 3");

            if(MainActivity.bu.getStatus() == null || MainActivity.bu.getStatus() != AsyncTask.Status.RUNNING){
                Log.d("status", "async task not running");
                MainActivity.bu = new background_uploading(ctx);
                MainActivity.bu.execute();
            }

        }
        else {
            Log.d("Network Available ", "Flag No 1 not available");

        }
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
}
