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

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by durgesh on 5/10/17.
 */

public class Internet_Broadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Network Available ", "Flag No 1 entered");

        SharedPreferences pref = context.getSharedPreferences("ActivityPREF" , 0);
        String username = pref.getString("username" , "user");

        if (isNetworkAvailable(context)) {
            Log.d("Network Available ", "Intent sent");
            Intent intent1= new Intent(context,background_uploading.class);
            context.startService(intent1);

        }
        else {
            Log.d("Network Available ", "Flag No 1 not available");

        }
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
}
