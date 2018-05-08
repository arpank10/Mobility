package com.example.ark.ark.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ark.ark.Constants;
import com.example.ark.ark.R;

import com.example.ark.ark.Services.DataRecording;
import com.example.ark.ark.Services.background_uploading;
import com.example.ark.ark.fragments.AccFragment;
import com.example.ark.ark.fragments.GpsFragment;
import com.example.ark.ark.fragments.GyroFragment;
import com.example.ark.ark.fragments.HomeFragment;
import com.example.ark.ark.fragments.MagFragment;
import com.example.ark.ark.fragments.RotationFragment;


import java.io.File;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Variables
    TextView user_text;
    String user_header_xml;
    //Internet_Broadcast Ireciever = new Internet_Broadcast();
    //public static background_uploading bu = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(this);
        String k=pref.getString("defaultm","abcd");
        //Toast.makeText(this,k,Toast.LENGTH_LONG).show();
        SharedPreferences.Editor ed=pref.edit();
        ed.putString("Mode",k);
        ed.commit();

        //String kq=pref.getString("Mode","abcd");

        //Toast.makeText(this,kq,Toast.LENGTH_LONG).show();

        // creating Directory Data here after checking corresponding permission
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
            //Directory variable
            File sdDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.DIRECTORY);
            File accDirectory=new File(sdDirectory+Constants.DIRECTORY_ACC);
            File magDirectory=new File(sdDirectory+Constants.DIRECTORY_MAG);
            File gpsDirectory=new File(sdDirectory+Constants.DIRECTORY_GPS);
            File gyroDirectory=new File(sdDirectory+Constants.DIRECTORY_GYRO);
            File rotationDirectory= new File(sdDirectory+Constants.DIRECTORY_ROTATION);
            //Creating respective directories
            if(sdDirectory.exists() && sdDirectory.isDirectory()){}
            else{sdDirectory.mkdirs();}
            if(accDirectory.exists()&&accDirectory.isDirectory()){}
            else {accDirectory.mkdirs();}
            if(magDirectory.exists() && magDirectory.isDirectory()){}
            else {magDirectory.mkdirs();}
            if(gpsDirectory.exists() && gpsDirectory.isDirectory()){}
            else{gpsDirectory.mkdirs();}
            if(gyroDirectory.exists() && gyroDirectory.isDirectory()){}
            else{gyroDirectory.mkdirs();}
            if(rotationDirectory.exists()&&rotationDirectory.isDirectory()){}
            else{rotationDirectory.mkdirs();}
        }
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        //Display GPS prompt if not enabled
        if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            displayGPSprompt();
        //Start recording if not running
        if(!isMyServiceRunning(DataRecording.class)) {
            startrecording();
        }
        if(!isMyServiceRunning(background_uploading.class)){
            Intent intent1= new Intent(this,background_uploading.class);
            startService(intent1);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //code added for username for header
        View header=navigationView.getHeaderView(0);

        SharedPreferences x = getSharedPreferences("ActivityPREF" , 0);
        user_header_xml = x.getString("username" , "user");
        TextView user_header_text  = (TextView) header.findViewById(R.id.user_header_name);
        user_header_text.setText(user_header_xml);

        //add this line to display menu1 when the activity is loaded
        if(savedInstanceState==null)
            displaySelectedScreen(R.id.nav_home);


        //start_uploading();

    }
    //Function to start data uploading to server
   /* public void start_uploading(){
        Log.d("status", "async task out  main activity");

        if( bu == null || bu.getStatus() != AsyncTask.Status.RUNNING){
            Log.d("status", "async task main activity");
            bu = new background_uploading(this);
            bu.execute();
        }
    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        /*registerReceiver( this.Ireciever ,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));*/
    }
    @Override
    public void onPause(){
        super.onPause();
        //unregisterReceiver( this.Ireciever );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    //Function to initiate operation based on option clicked on toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch(id) {
            case R.id.stop:
                stoprecording();
                return true;
            case R.id.start:
                startrecording();
                return true;
            case R.id.about_us:
                startActivity(new Intent(this,AboutUsActivity.class));
                return true;
            case R.id.help:
                startActivity(new Intent(this,HelpActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Display the selected fragment
    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_accelerometer:
                fragment = new AccFragment();
                break;
            case R.id.nav_magnetometer:
                fragment = new MagFragment();
                break;
            case R.id.nav_gps:
                fragment = new GpsFragment();
                break;
            case R.id.nav_rotation:
                fragment = new RotationFragment();
                break;
            case R.id.nav_gyro:
                fragment = new GyroFragment();
                break;
            case R.id.nav_settings:
                Intent intent1=new Intent(this,SettingsActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_view_files:
            {
                //Sending intent to file manager
                Uri uri=Uri.parse("/sdcard/Mobility/");
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri,"resource/folder");
                if (intent.resolveActivityInfo(getPackageManager(), 0) != null)
                {
                    startActivity(Intent.createChooser(intent, "Open folder"));
                }
                else
                {
                    Toast toast=Toast.makeText(this,"No File Manager Installed",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        //make this method blank
        return true;
    }

    //Function to check if data recording is running or not
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    //Function to stop data recording
    public void stoprecording()
    {
        if(isMyServiceRunning(DataRecording.class))
        {
            Intent intent=new Intent(this,DataRecording.class);
            stopService(intent);
            Toast.makeText(this,"Data Recording Stopped",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,"Data Recording is currently not active",Toast.LENGTH_SHORT).show();
        }
    }

    //Function to start Data Recording
    public void startrecording()
    {
        if(!isMyServiceRunning(DataRecording.class))
        {
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                Toast toast=Toast.makeText(getApplicationContext(), "Recording started",Toast.LENGTH_SHORT);
                toast.show();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("ActivityPREF",0);
                String s=pref.getString("username", "user");
                SharedPreferences pref1= PreferenceManager.getDefaultSharedPreferences(this);
                String acc_mag_freq=pref1.getString("acc_mag_frequency","100");
                String def=pref1.getString("defaultm","2");
                String gps_freq=pref1.getString("gps_frequency","2000");
                String recording_mode=pref1.getString("Mode","2");
                Intent intent=new Intent(this, DataRecording.class);
                intent.putExtra("username",s);
                intent.putExtra("acc_mag_freq",acc_mag_freq);
                intent.putExtra("gps_freq",gps_freq);
                intent.putExtra("mode",recording_mode);
                startService(intent);
            }
            else
                Toast.makeText(this,"You do not have the required permissions",Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this,"Data recording is already active",Toast.LENGTH_SHORT).show();
    }


    //Display GPS prompt if GPS is off
    public void displayGPSprompt(){
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "GPS is currently switched off"+" Switch it on for high location accuracy";
        builder.setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                startActivity(new Intent(action));
                                d.dismiss();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                d.cancel();
                            }
                        });
        builder.create().show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /*
    private BroadcastReceiver Ireciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }

    };
    */
}