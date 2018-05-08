package com.example.ark.ark.Services;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.ark.ark.Constants;
import com.example.ark.ark.activity.UnlockMobileActivity;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by ark on 5/11/17.
 */

public class ScreenOn extends IntentService {

    private Timer screenTimer = null;
    public static final int notify = 30000;
    private Handler mHandler ;
    private boolean SCREEN_ON=false;
    private BroadcastReceiver receiver;
    public ScreenOn() {
        super("Collection");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        String man=Build.MANUFACTURER;
        Toast.makeText(this, man, Toast.LENGTH_SHORT).show();
        if(man.equalsIgnoreCase("Huawei") || man.equalsIgnoreCase("Xiaomi"))
            SCREEN_ON=true;
        if(SCREEN_ON)
        {
            final IntentFilter intentFilter = new IntentFilter("android.intent.action.SCREEN_OFF");
            intentFilter.addAction("android.intent.action.SCREEN_ON");
            this.receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                        Log.d("Receiver","screen off ");
                        Constants.screen=0;
                    } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                        Constants.screen = 1;
                        Log.d("Receiver", "screen on ");

                    }
                }
            };

            this.registerReceiver(this.receiver, intentFilter);
        }

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(SCREEN_ON)
            Log.d("Screen", "on");
        mHandler = new Handler();
        if(SCREEN_ON) {
            if (screenTimer != null)
                screenTimer.cancel();
            else
                screenTimer = new Timer();
            screenTimer.scheduleAtFixedRate(new ScreenOn.TimeDisplay(), 0, notify);
            Log.d("ScreenOnservice", "This service started");
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if(SCREEN_ON)
            this.unregisterReceiver(this.receiver);
        if (screenTimer != null)
            screenTimer.cancel();
        super.onDestroy();
    }

    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(Constants.screen==0){
                        Intent intent=new Intent(getApplicationContext(), UnlockMobileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Log.d("MobileScreen","Unlock");
                        startActivity(intent);}
                    }
            });
        }
    }

}
