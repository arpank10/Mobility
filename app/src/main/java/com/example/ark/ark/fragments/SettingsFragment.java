package com.example.ark.ark.fragments;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ark.ark.R;
import com.example.ark.ark.Services.DataRecording;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment {

    SharedPreferences pref;

    public SettingsFragment() {
        setValues();
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setValues();
        getActivity().setTitle("Settings");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setValues();
        addPreferencesFromResource(R.xml.preferences);
        Preference button = findPreference(getString(R.string.save_button));
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                setValues();
                restartRecording();
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
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("ActivityPREF",0);
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
    public void setValues(){
        Preference mode=findPreference("Mode");
        Log.d("Connected","asdsadasd");
        if (mode instanceof ListPreference) {
            ListPreference listPref = (ListPreference) mode;
            Log.d("Connected","abcd");
            mode.setSummary(listPref.getEntry());
        }
        mode=findPreference("defaultm");
        if(mode instanceof ListPreference){
            ListPreference listr=(ListPreference) mode;
            mode.setSummary(listr.getEntry());
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
