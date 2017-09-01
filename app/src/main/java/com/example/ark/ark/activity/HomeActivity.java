package com.example.ark.ark.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ark.ark.Constants;
import com.example.ark.ark.R;

/**
 * Created by ark on 15/8/17.
 */

public class HomeActivity extends AppCompatActivity {
    private EditText e;
    private Button b;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if(pref.getBoolean("activity_executed", false)){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        SharedPreferences.Editor ed = pref.edit();
        ed.putBoolean("activity_executed", true);
        ed.commit();
        setContentView(R.layout.home_activity_layout);
        //Dealing with permissions
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        e=(EditText)findViewById(R.id.user_name);
        b=(Button)findViewById(R.id.button_tostart);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s=e.getText().toString();
                if(s.length()==0)
                    displayError();
                if(s.length()!=0){
                    SharedPreferences pref1 = getSharedPreferences("username",0);
                    SharedPreferences.Editor editor=pref1.edit();
                    editor.putString("user",s);
                    editor.commit();
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);}
            }
        });
    }
    public void displayError(){
        Toast toast=Toast.makeText(getApplicationContext(), "Enter name of atleast one letter",Toast.LENGTH_SHORT);
        toast.show();
    }

    //Checking if the app has required permissions
    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}
