package com.example.ark.ark.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.ark.ark.R;
import com.example.ark.ark.fragments.SettingsFragment;

/**
 * Created by ark on 30/8/17.
 */

public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getFragmentManager().beginTransaction().replace(android.R.id.content,new SettingsFragment()).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
