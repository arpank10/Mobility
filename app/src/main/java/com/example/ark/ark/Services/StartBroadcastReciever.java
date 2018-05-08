package com.example.ark.ark.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ark on 21/10/17.
 */

public class StartBroadcastReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1=new Intent(context,DataRecording.class);
        SharedPreferences pref = context.getSharedPreferences("ActivityPREF",0);
        String s=pref.getString("username", "user");
        SharedPreferences pref1= PreferenceManager.getDefaultSharedPreferences(context);
        String acc_mag_freq=pref1.getString("acc_mag_frequency","100");
        String gps_freq=pref1.getString("gps_frequency","2000");
        String recording_mode=pref1.getString("Mode","2");
        intent1.putExtra("username",s);
        intent1.putExtra("acc_mag_freq",acc_mag_freq);
        intent1.putExtra("gps_freq",gps_freq);
        intent1.putExtra("mode",recording_mode);
        context.startService(intent1);
    }
}
