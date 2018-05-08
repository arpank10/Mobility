package com.example.ark.ark.activity;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.example.ark.ark.R;

public class UnlockMobileActivity extends AppCompatActivity {
    private PowerManager.WakeLock fullWakeLock,partialWakeLock;

    private void UnlockMobileActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("unlock","started activity");
        setContentView(R.layout.activity_unlock_mobile);

        createWakeLocks();
        ScreenOnMethod();
        if (fullWakeLock.isHeld()) {
            fullWakeLock.release();
        }
        if (partialWakeLock.isHeld()) {
            partialWakeLock.release();
        }
        Log.d("UnlockActivity", "Finished");
        finish();

    }

    @Override
    protected void onPause() {
        super.onPause();
        partialWakeLock.acquire();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(fullWakeLock.isHeld()){
            fullWakeLock.release();
        }
        if(partialWakeLock.isHeld()){
            partialWakeLock.release();
        }
    }

    protected void createWakeLocks(){
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        fullWakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP) | PowerManager.ON_AFTER_RELEASE, "Fullwakelock");
        partialWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Partiallock");
    }

    public void ScreenOnMethod() {
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();
        fullWakeLock.acquire();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        //getWindow().addFlags( WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        Log.d("ScreenOnService","Device waked up now");
    }

}