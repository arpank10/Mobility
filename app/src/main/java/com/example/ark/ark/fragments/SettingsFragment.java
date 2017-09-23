package com.example.ark.ark.fragments;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.example.ark.ark.R;
import com.example.ark.ark.Services.DataRecording;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment {


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Settings");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        Preference button = findPreference(getString(R.string.save_button));
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                restartRecording();
                return true;
            }
        });
        SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(getActivity());
        String m=pref.getString("Mode",null);
        Preference mode=findPreference("Mode");
        if(m=="1")
            mode.setSummary(R.string.include);
        else if(m=="2")
            mode.setSummary(R.string.exclude);
        mode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String newval=o.toString();
                if(newval=="1")
                    preference.setSummary(R.string.include);
                else if(newval=="2")
                    preference.setSummary(R.string.exclude);
                return true;
            }
        });
       Preference def_mode=findPreference("defaultm");
        def_mode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String newval=o.toString();
                if(newval=="1")
                   preference.setSummary(R.string.include);
                else if(newval=="2")
                preference.setSummary(R.string.exclude);
                return true;
            }
        });
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void restartRecording() {
        SharedPreferences pref = getActivity().getSharedPreferences("username", 0);
        String s = pref.getString("user", "");
        SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String acc_mag_freq = pref1.getString("acc_mag_frequency", "100");
        String gps_freq = pref1.getString("gps_frequency", "2000");
        String recording_mode=pref1.getString("Mode","2");
        Intent intent = new Intent(getActivity(), DataRecording.class);
        intent.putExtra("username", s);
        intent.putExtra("acc_mag_freq", acc_mag_freq);
        intent.putExtra("gps_freq", gps_freq);
        intent.putExtra("mode",recording_mode);
        if (isMyServiceRunning(DataRecording.class)) {
            Intent intentStop = new Intent(getActivity(), DataRecording.class);
            getActivity().stopService(intentStop);
            getActivity().startService(intent);
        } else {
            getActivity().startService(intent);
        }
        Toast.makeText(getActivity(),recording_mode,Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "Data recording restarted", Toast.LENGTH_SHORT).show();
    }
}
