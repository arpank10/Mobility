package com.example.ark.ark.activity;

import android.Manifest;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ark.ark.R;
import com.example.ark.ark.Services.uploading;
import com.example.ark.ark.app_url.app_config;
import java.util.HashMap;
import java.util.Map;



/**
 * Created by ark on 15/8/17.
 */

public class HomeActivity extends AppCompatActivity{
    private EditText e;
    private Button b;

    private  EditText inputusername;
    private EditText inputpassword;
    private EditText inputemail;
    private EditText inputfullname;
    private Button btnRegister;

    public static String username_public;


    private Context ctx;
    private AlertDialog pDialog;
    SharedPreferences pref , pref1;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_layout);


        pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if(pref.getBoolean("activity_executed", false)){
            app_config.User_name_app_config = pref.getString("username" , "");

            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        btnRegister = (Button) findViewById(R.id.button_tostart);
        ctx = this;

        //Dealing with permissions
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                inputfullname = (EditText) findViewById(R.id.full_name);
                inputemail = (EditText) findViewById(R.id.email);
                inputpassword = (EditText) findViewById(R.id.pass_word);
                inputusername = (EditText) findViewById(R.id.user_name);

                 final String name  , username , password , email;

                 name = inputfullname.getText().toString();
                  username = inputusername.getText().toString();
                 password = inputpassword.getText().toString();
                 email = inputemail.getText().toString();

                username_public =  username;
               // pref1 = getApplicationContext().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();

                editor.putString("username", username);
                editor.putString("fullname" , name);
                editor.putString("password", password);
                editor.putString("email", email);
                editor.commit();


                if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !name.isEmpty()) {

                if(isNetworkAvailable()) {
                   register(username  , name , password , email );

                }
               else{
                        Toast.makeText(getApplicationContext(), "Network Not Available Check Connection !!", Toast.LENGTH_LONG).show();
                    }
                }

                else {
                    Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();}
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

    private  void register(final  String username , final String name , final String password , final String email){



        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        String url = app_config.URL_REGISTER;
        final RequestQueue requestQueue =Volley.newRequestQueue(ctx);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("durgesh", "Register Response: " + response.toString());
                        if(response.contains("success")){
                            requestQueue.stop();
                            Intent intent = new Intent(ctx.getApplicationContext(), MainActivity.class);

                            SharedPreferences.Editor ed = pref.edit();
                            ed.putBoolean("activity_executed", true);
                            ed.commit();


                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "already registered" , Toast.LENGTH_SHORT).show();
                        }

                    }
                } , new Response.ErrorListener() {
                       @Override
                         public void onErrorResponse(VolleyError error) {
                              error.printStackTrace();
                           Log.e("durgesh", "Registration Error: " + error.getMessage());
                           Toast.makeText(getApplicationContext(), "Failed to connect to server,network unreachable.\nCheck Connection", Toast.LENGTH_LONG).show();

                       }
                 }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:
                params.put("username", username );
                params.put("fullname" , name);
                params.put("password", password);
                params.put("email" , email);
                return params;
            }
        };
        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        postRequest.setRetryPolicy(policy);


        requestQueue.add(postRequest);
    }





    public  boolean isNetworkAvailable() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnectedOrConnecting())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnectedOrConnecting())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

}
