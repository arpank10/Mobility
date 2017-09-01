package com.example.ark.ark.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ark.ark.Constants;
import com.example.ark.ark.R;

import com.example.ark.ark.Services.DataRecording;
import com.example.ark.ark.fragments.AccFragment;
import com.example.ark.ark.fragments.GpsFragment;
import com.example.ark.ark.fragments.HomeFragment;
import com.example.ark.ark.fragments.MagFragment;
import com.example.ark.ark.fragments.SettingsFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // creating Directory Data here after checking corresponding permission
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
            File sdDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.DIRECTORY);
            File accDirectory=new File(sdDirectory+Constants.DIRECTORY_ACC);
            File magDirectory=new File(sdDirectory+Constants.DIRECTORY_MAG);
            File gpsDirectory=new File(sdDirectory+Constants.DIRECTORY_GPS);
            if(sdDirectory.exists() && sdDirectory.isDirectory()){
                //directory is present, no need to do anything here
            }
            else{
                sdDirectory.mkdirs();
            }
            if(accDirectory.exists()&&accDirectory.isDirectory()){}
            else {accDirectory.mkdirs();}
            if(magDirectory.exists() && magDirectory.isDirectory()){}
            else {magDirectory.mkdirs();}
            if(gpsDirectory.exists() && gpsDirectory.isDirectory()){}
            else{gpsDirectory.mkdirs();}
        }

        if(!isMyServiceRunning(DataRecording.class))
            startrecording();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //add this line to display menu1 when the activity is loaded
        displaySelectedScreen(R.id.nav_home);
    }


    private void permissioncheck() {
        int permissionloc = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission_group.LOCATION);
        int storagePermission = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission_group.STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission_group.STORAGE);
        }
        if (permissionloc!= PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission_group.LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),1);
        }
    }

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

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
            case R.id.nav_settings:
                Intent intent1=new Intent(this,SettingsActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_view_files:
            {
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
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
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
    public void startrecording()
    {
        if(!isMyServiceRunning(DataRecording.class))
        {
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                Toast toast=Toast.makeText(getApplicationContext(), "Recording started",Toast.LENGTH_SHORT);
                toast.show();
                SharedPreferences pref =getSharedPreferences("username",0);
                String s=pref.getString("user","");
                SharedPreferences pref1= PreferenceManager.getDefaultSharedPreferences(this);
                String acc_mag_freq=pref1.getString("acc_mag_frequency","100");
                String gps_freq=pref1.getString("gps_frequency","2000");
                Intent intent=new Intent(this, DataRecording.class);
                intent.putExtra("username",s);
                intent.putExtra("acc_mag_freq",acc_mag_freq);
                intent.putExtra("gps_freq",gps_freq);
                startService(intent);
            }
            else
                Toast.makeText(this,"You do not have the required permissions",Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this,"Data recording is already active",Toast.LENGTH_SHORT).show();
    }


}