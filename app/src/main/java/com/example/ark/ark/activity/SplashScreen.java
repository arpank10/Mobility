package com.example.ark.ark.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.ark.ark.R;

/**
 * Created by ark on 9/8/17.
 */

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);

        Thread new_thread=new Thread(){
            @Override
            public void run() {
                try
                {
                    sleep(2000);
                    Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new_thread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}